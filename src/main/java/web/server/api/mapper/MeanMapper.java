package web.server.api.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface MeanMapper {

    int deleteById(int vocId);
}
