package web.server.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import web.server.api.entity.UserEntity;

import java.util.List;
import java.util.Map;

@Mapper
public interface OAuth2Mapper {

    int deleteByUsername(String username);
}
