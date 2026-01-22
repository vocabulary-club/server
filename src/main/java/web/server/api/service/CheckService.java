package web.server.api.service;

import web.server.api.mapper.CheckMapper;
import web.server.api.mapper.ManageMapper;
import org.springframework.stereotype.Service;

@Service
public class CheckService {

    private final CheckMapper checkRepository;
    private final ManageMapper manageRepository;

    public CheckService(CheckMapper checkRepository, ManageMapper manageRepository) {
        this.checkRepository = checkRepository;
        this.manageRepository = manageRepository;
    }

    public Object select() {
        return checkRepository.select();
    }

}
