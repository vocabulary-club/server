package web.server.api.service;

import org.springframework.stereotype.Service;
import web.server.api.entity.UserEntity;
import web.server.api.mapper.UserMapper;

@Service
public class UserService {

    private final UserMapper userRepository;

    public UserService(UserMapper userRepository) {

        this.userRepository = userRepository;
    }

    public UserEntity selectByUsername(String username) {

        return userRepository.selectByUsername(username);
    }

    public int insert(UserEntity entity) {
        return userRepository.insert(entity);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username) > 0;
    }

}