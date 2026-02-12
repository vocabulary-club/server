package web.server.api.service;

import jakarta.servlet.http.Cookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import web.server.api.entity.UserEntity;
import web.server.api.mapper.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

@Service
public class ManageService {

    private final ManageMapper manageMapper;
    private final UserMapper userMapper;
    private final DicMapper dicMapper;
    private final WordMapper wordMapper;
    private final MeanMapper meanMapper;

    public ManageService(ManageMapper manageMapper,
                         UserMapper userMapper,
                         DicMapper dicMapper,
                         WordMapper wordMapper,
                         MeanMapper meanMapper) {
        this.manageMapper = manageMapper;
        this.userMapper = userMapper;
        this.dicMapper = dicMapper;
        this.wordMapper = wordMapper;
        this.meanMapper = meanMapper;
    }

    public Object create(Map<String, Object> data) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        UserEntity entity = userMapper.selectByUsername(username);
        if(entity == null) {
            return 0;
        }
        data.put("user_id", entity.getId());

        int nEngId = manageMapper.insertVocEng(data);
        int nMonId = manageMapper.insertVocMon(data);
        if(nEngId == 1 && nMonId == 1) {
            return manageMapper.insertVocDic(data);
        }
        return 0;
    }

    public Object update(Map<String, Object> data) {

        int nEngId = manageMapper.updateVocEng(data);
        int nMonId = manageMapper.updateVocMon(data);

        return 0;
    }

    public Object delete(Map<String, Object> data) {

        int nDicId = manageMapper.deleteVocDic(data);
        int nEngId = manageMapper.deleteVocEng(data);
        int nMonId = manageMapper.deleteVocMon(data);

        return 0;
    }

    public Object select() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        UserEntity entity = userMapper.selectByUsername(username);
        if(entity == null) {
            return Collections.emptyList();
        }

        return manageMapper.select(entity);
    }

    public Object selectByUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        UserEntity entity = userMapper.selectByUsername(username);
        if(entity == null) {
            return Collections.emptyList();
        }

        return manageMapper.selectByUserId(entity.getId());
    }

    public Object deleteByUserId(int userId) {

        List<Map<String, Object>> dicList =  dicMapper.selectByUserId(userId);
        for (Map<String, Object> dic : dicList) {
            String engVocId = dic.get("eng_id").toString();
            String monVocId = dic.get("mon_id").toString();
            wordMapper.deleteById(Integer.parseInt(engVocId));
            meanMapper.deleteById(Integer.parseInt(monVocId));
        }
        dicMapper.deleteByUserId(userId);

        return 0;
    }
}
