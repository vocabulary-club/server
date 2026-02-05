package web.server.api.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import web.server.api.entity.UserEntity;
import web.server.api.mapper.ManageMapper;
import org.springframework.stereotype.Service;
import web.server.api.mapper.UserMapper;

import java.util.Collections;
import java.util.Map;

@Service
public class ManageService {

    private final ManageMapper manageMapper;
    private final UserMapper userMapper;

    public ManageService(ManageMapper manageMapper, UserMapper userMapper) {
        this.manageMapper = manageMapper;
        this.userMapper = userMapper;
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
}
