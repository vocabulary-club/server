package web.server.api.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import web.server.api.entity.UserEntity;
import web.server.api.mapper.MeanMapper;
import web.server.api.mapper.UserMapper;
import web.server.api.mapper.WordMapper;

import java.util.Collections;

@Service
public class TestService {

    private final MeanMapper meanMapper;
    private final WordMapper wordMapper;
    private final UserMapper userMapper;

    public TestService(MeanMapper meanMapper,
                       WordMapper wordMapper,
                       UserMapper userMapper) {
        this.meanMapper = meanMapper;
        this.wordMapper = wordMapper;
        this.userMapper = userMapper;
    }

    public Object select() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        UserEntity entity = userMapper.selectByUsername(username);
        if(entity == null) {
            return Collections.emptyList();
        }

        return meanMapper.selectByUserId(entity.getUserId());
    }

    public Object selectDate() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        UserEntity entity = userMapper.selectByUsername(username);
        if(entity == null) {
            return Collections.emptyList();
        }

        return wordMapper.selectDateByUserId(entity.getUserId());
    }

}
