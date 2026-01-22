package web.server.api.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ManageMapper {

    int insertVocEng(Map<String, Object> data);
    int insertVocMon(Map<String, Object> data);
    int insertVocDic(Map<String, Object> data);
    int updateVocEng(Map<String, Object> data);
    int updateVocMon(Map<String, Object> data);
    int deleteVocDic(Map<String, Object> data);
    int deleteVocEng(Map<String, Object> data);
    int deleteVocMon(Map<String, Object> data);
    List<Map<String, Object>> select();
}
