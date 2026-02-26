package web.server.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import web.server.api.entity.UserEntity;

@Mapper
public interface UserMapper {

    UserEntity selectByUsername(String username);

    int insert(UserEntity entity);

    int update(UserEntity entity);

    int deleteByUsername(String username);

    int existsByUsername(UserEntity entity);
}