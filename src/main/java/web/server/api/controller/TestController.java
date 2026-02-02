package web.server.api.controller;

import web.server.api.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final TestService checkService;

    public TestController(TestService checkService) {
        this.checkService = checkService;
    }

    @GetMapping("/select")
    public Object select() {
        return checkService.select();
    }
}