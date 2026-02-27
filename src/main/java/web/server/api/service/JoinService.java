package web.server.api.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import web.server.api.dto.JoinDTO;
import web.server.api.entity.MailVerificationTokenEntity;
import web.server.api.entity.UserEntity;
import web.server.api.mapper.UserMapper;
import web.server.api.utility.MailVerificationTokenUtility;

import java.time.Instant;

@Service
public class JoinService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MailVerificationTokenService mailVerificationTokenService;
    private final SecretService secretService;

    private final MailService mailService;

    public JoinService(UserMapper userMapper,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       MailVerificationTokenService mailVerificationTokenService,
                       SecretService secretService,
                       MailService mailService) {

        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mailVerificationTokenService = mailVerificationTokenService;
        this.secretService = secretService;
        this.mailService = mailService;
    }

    public void join(JoinDTO dto) {

        UserEntity userEntity = new UserEntity();
        userEntity.setProvider("local");
        userEntity.setUsername(dto.getUsername());
        userEntity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        userEntity.setName(dto.getName());
        userEntity.setEmail(dto.getEmail());
        userEntity.setRole("ROLE_USER");
        userEntity.setVerified('N');

        if (userMapper.existsByUsername(userEntity) > 0) {
            throw new IllegalStateException("Username already exists");
        }

        int result = userMapper.insert(userEntity);
        if(result > 0) {

            String token = MailVerificationTokenUtility.generate();

            MailVerificationTokenEntity tokenEntity = new MailVerificationTokenEntity();
            tokenEntity.setToken(token);
            tokenEntity.setExpiration(Instant.now().plusMillis(secretService.getMailVerificationTokenExpire()));
            mailVerificationTokenService.insert(tokenEntity);

            mailService.sendMail(
                    dto.getEmail(),
                    "[SHINE-UG] Mail Verification",
                    "https://www.shineug.com/verify?token=" + token
            );
        }
    }
}
