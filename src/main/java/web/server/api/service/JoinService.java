package web.server.api.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import web.server.api.dto.JoinDTO;
import web.server.api.entity.UserEntity;

@Service
public class JoinService {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserService userService,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void join(JoinDTO dto) {

        if (userService.existsByUsername(dto.getUsername())) {
            throw new IllegalStateException("Username already exists");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(dto.getUsername());
        userEntity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        userEntity.setRole("ROLE_USER");

        userService.insert(userEntity);
    }
}