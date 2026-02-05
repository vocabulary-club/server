package web.server.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import web.server.api.entity.UserEntity;

import java.util.List;
import java.util.Map;

@Mapper
public interface HomeMapper {

    Map<String, Object> selectLastDate(UserEntity entity);
    Map<String, Object> selectSecondLastDate(UserEntity entity);
    Map<String, Object> selectThirdLastDate(UserEntity entity);
    List<Map<String, Object>> select(Map<String, Object> data);
}
