package web.server.api.service;

import web.server.api.mapper.TestMapper;
import web.server.api.mapper.ManageMapper;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private final TestMapper checkRepository;
    private final ManageMapper manageRepository;

    public TestService(TestMapper checkRepository, ManageMapper manageRepository) {
        this.checkRepository = checkRepository;
        this.manageRepository = manageRepository;
    }

    public Object select() {
        return checkRepository.select();
    }

}
