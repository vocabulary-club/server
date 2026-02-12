package web.server.api.service;

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

    private final UserMapper userMapper;
    private final WordMapper wordMapper;
    private final MeanMapper meanMapper;

    public ManageService(UserMapper userMapper,
                         WordMapper wordMapper,
                         MeanMapper meanMapper) {
        this.userMapper = userMapper;
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
        data.put("userId", entity.getId());

        int result = wordMapper.insert(data);
        if(result == 1) { meanMapper.insert(data); }
        return result;
    }

    public Object update(Map<String, Object> data) {

        int result = wordMapper.update(data);
        if(result == 1) { meanMapper.update(data); }
        return result;
    }

    public Object delete(Map<String, Object> data) {

        int result = meanMapper.delete(data);
        if(result == 1) { wordMapper.delete(data); }
        return result;
    }

    public Object selectByUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        UserEntity entity = userMapper.selectByUsername(username);
        if(entity == null) {
            return Collections.emptyList();
        }

        return wordMapper.selectByUserId(entity.getId());
        //return meanMapper.selectByUserId(entity.getId());
    }

    public Object deleteByUserId(int userId) {

        List<Map<String, Object>> wordList =  meanMapper.selectByUserId(userId);
        for (Map<String, Object> word : wordList) {
            int meanId = Integer.parseInt(word.get("mean_id").toString());
            meanMapper.deleteByMeanId(meanId);
        }
        wordMapper.deleteByUserId(userId);

        return 0;
    }
}
