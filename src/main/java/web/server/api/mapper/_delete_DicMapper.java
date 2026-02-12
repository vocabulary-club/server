package web.server.api.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface _delete_DicMapper {

    List<Map<String, Object>> selectByUserId(int userId);
    int deleteByUserId(int userId);
}
