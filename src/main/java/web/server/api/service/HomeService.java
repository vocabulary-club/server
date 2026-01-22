package web.server.api.service;

import web.server.api.mapper.HomeMapper;
import web.server.api.mapper.ManageMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HomeService {

    private final HomeMapper homeRepository;
    private final ManageMapper manageRepository;

    public HomeService(HomeMapper homeRepository, ManageMapper manageRepository) {
        this.homeRepository = homeRepository;
        this.manageRepository = manageRepository;
    }

    public Object select(Map<String, Object> data) {

        Map<String, Object> lastRegDate = null;

        String strDate = data.get("day").toString();
        if(strDate.equals("last")) {

            lastRegDate = homeRepository.selectLastDate();

        } else if(strDate.equals("second last")) {

            lastRegDate = homeRepository.selectSecondLastDate();

        } else if(strDate.equals("third last")) {

            lastRegDate = homeRepository.selectThirdLastDate();

        }

        return homeRepository.select(lastRegDate);
    }

}
