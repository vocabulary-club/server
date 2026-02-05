package web.server.api.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import web.server.api.entity.UserEntity;
import web.server.api.mapper.TestMapper;
import web.server.api.mapper.UserMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class TestService {

    private final TestMapper testMapper;
    private final UserMapper userMapper;

    public TestService(TestMapper testMapper, UserMapper userMapper) {
        this.testMapper = testMapper;
        this.userMapper = userMapper;
    }

    public Object select() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        UserEntity entity = userMapper.selectByUsername(username);
        if(entity == null) {
            return Collections.emptyList();
        }

        return testMapper.select(entity);
    }

}
