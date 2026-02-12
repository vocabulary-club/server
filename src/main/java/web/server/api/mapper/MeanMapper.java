package web.server.api.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MeanMapper {

    List<Map<String, Object>> selectByUserId(int userId);
    int insert(Map<String, Object> data);
    int update(Map<String, Object> data);
    int delete(Map<String, Object> data);
    int deleteByMeanId(int meanId);
}
