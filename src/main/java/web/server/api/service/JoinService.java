package web.server.api.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import web.server.api.dto.JoinDTO;
import web.server.api.entity.UserEntity;
import web.server.api.mapper.UserMapper;

@Service
public class JoinService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserMapper userMapper,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void join(JoinDTO dto) {

        UserEntity userEntity = new UserEntity();
        userEntity.setProvider("local");
        userEntity.setUsername(dto.getUsername());
        userEntity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        userEntity.setName(dto.getName());
        userEntity.setEmail(dto.getEmail());
        userEntity.setRole("ROLE_USER");

        if (userMapper.existsByUsername(userEntity) > 0) {
            throw new IllegalStateException("Username already exists");
        }

        userMapper.insert(userEntity);
    }
}
