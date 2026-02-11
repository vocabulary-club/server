package web.server.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import web.server.api.entity.UserEntity;

import java.util.List;
import java.util.Map;

@Mapper
public interface DicMapper {

    List<Map<String, Object>> selectByUserId(int userId);
    int deleteByUserId(int userId);
}
