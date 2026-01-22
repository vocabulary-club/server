package web.server.api.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CheckMapper {

    List<Map<String, Object>> select();
}
