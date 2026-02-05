package web.server.api.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.server.api.entity.UserEntity;
import web.server.api.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/user")
    public Object user() {

        return userService.selectByUsername();
    }
}