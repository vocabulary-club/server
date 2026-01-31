package web.server.api.service;

import org.springframework.stereotype.Service;
import web.server.api.entity.UserEntity;
import web.server.api.mapper.UserMapper;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {

        this.userMapper = userMapper;
    }

    public UserEntity selectByUsername(String username) {

        return userMapper.selectByUsername(username);
    }
}