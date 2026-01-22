package web.server.api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.server.api.dto.MyUserDetails;
import web.server.api.entity.UserEntity;
import web.server.api.mapper.UserMapper;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserMapper userRepository;

    public MyUserDetailsService(UserMapper userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.selectByUsername(username);

        if (userEntity != null) {

            return new MyUserDetails(userEntity);
        }

        return null;
    }
}