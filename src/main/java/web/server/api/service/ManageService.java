package web.server.api.service;

import web.server.api.mapper.ManageMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ManageService {

    private final ManageMapper manageRepository;

    public ManageService(ManageMapper manageRepository) {
        this.manageRepository = manageRepository;
    }

    public Object create(Map<String, Object> data) {

        int nEngId = manageRepository.insertVocEng(data);
        int nMonId = manageRepository.insertVocMon(data);
        if(nEngId == 1 && nMonId == 1) {
            return manageRepository.insertVocDic(data);
        }
        return 0;
    }

    public Object update(Map<String, Object> data) {

        int nEngId = manageRepository.updateVocEng(data);
        int nMonId = manageRepository.updateVocMon(data);

        return 0;
    }

    public Object delete(Map<String, Object> data) {

        int nDicId = manageRepository.deleteVocDic(data);
        int nEngId = manageRepository.deleteVocEng(data);
        int nMonId = manageRepository.deleteVocMon(data);

        return 0;
    }

    public Object select() {
        return manageRepository.select();
    }
}
