package web.server.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import web.server.api.entity.UserEntity;

import java.util.List;
import java.util.Map;

@Mapper
public interface WordMapper {

    List<Map<String, Object>> selectByUserId(int userId);
    int insert(Map<String, Object> data);
    int update(Map<String, Object> data);
    int delete(Map<String, Object> data);
    int deleteByWordId(int wordId);
}
