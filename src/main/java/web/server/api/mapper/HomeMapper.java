package web.server.api.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface HomeMapper {

    Map<String, Object> selectLastDate(int userId);
    Map<String, Object> selectSecondLastDate(int userId);
    Map<String, Object> selectThirdLastDate(int userId);
    List<Map<String, Object>> select(Map<String, Object> data);
}
