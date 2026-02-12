package web.server.api.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import web.server.api.entity.UserEntity;
import web.server.api.mapper.HomeMapper;
import web.server.api.mapper.UserMapper;

import java.util.Collections;
import java.util.Map;

@Service
public class HomeService {

    private final HomeMapper homeMapper;
    private final UserMapper userMapper;

    public HomeService(HomeMapper homeMapper, UserMapper userMapper) {
        this.homeMapper = homeMapper;
        this.userMapper = userMapper;
    }

    public Object select(Map<String, Object> data) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        UserEntity entity = userMapper.selectByUsername(username);
        if(entity == null) {
            return Collections.emptyList();
        }

        Map<String, Object> lastRegDate = null;

        String strDate = data.get("day").toString();
        if(strDate.equals("last")) {

            lastRegDate = homeMapper.selectLastDate(entity.getId());

        } else if(strDate.equals("second last")) {

            lastRegDate = homeMapper.selectSecondLastDate(entity.getId());

        } else if(strDate.equals("third last")) {

            lastRegDate = homeMapper.selectThirdLastDate(entity.getId());

        }
        if(lastRegDate == null) {
            return Collections.emptyList();
        }

        // user PK int
        lastRegDate.put("userId", entity.getId());

        return homeMapper.select(lastRegDate);
    }

}
