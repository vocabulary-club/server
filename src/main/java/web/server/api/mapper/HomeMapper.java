package web.server.api.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface HomeMapper {

    Map<String, Object> selectLastDate();
    Map<String, Object> selectSecondLastDate();
    Map<String, Object> selectThirdLastDate();
    List<Map<String, Object>> select(Map<String, Object> data);
}
