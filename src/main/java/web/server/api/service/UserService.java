package web.server.api.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import web.server.api.dto.UserDTO;
import web.server.api.entity.UserEntity;
import web.server.api.mapper.OAuth2Mapper;
import web.server.api.mapper.TokenMapper;
import web.server.api.mapper.UserMapper;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final ManageService manageService;
    private final OAuth2Mapper oauth2Mapper;
    private final TokenMapper tokenMapper;

    public UserService(UserMapper userMapper,
                       ManageService manageService,
                       OAuth2Mapper oauth2Mapper,
                       TokenMapper tokenMapper) {

        this.userMapper = userMapper;
        this.manageService = manageService;
        this.oauth2Mapper = oauth2Mapper;
        this.tokenMapper = tokenMapper;
    }

    public UserDTO selectByUsername() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        UserEntity entity = userMapper.selectByUsername(username);
        if(entity != null) {
            return entity.getUserDTO();
        }
        return null;
    }

    public Object delete() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        UserEntity entity = userMapper.selectByUsername(username);
        if(entity == null) {
            return 0;
        }

        // 1. delete voc_word, voc_mean by user_id
        manageService.deleteByUserId(entity.getId());

        // 2. delete oauth2_authorized_client by username = principal_name
        oauth2Mapper.deleteByUsername(username);

        // 3. delete voc_token by username
        // token will be deleted in MyLogoutHelder
        //tokenMapper.deleteByUsername(username);

        // 4. delete voc_user by username
        userMapper.deleteByUsername(username);

        // 5. logout in controller

        return 1;
    }
}