package web.server.api.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.server.api.service.MailVerificationTokenService;

import java.io.IOException;
import java.util.Map;

@RestController
public class MailVerificationTokenController {

    private static final Logger log = LoggerFactory.getLogger(MailVerificationTokenController.class);

    private final MailVerificationTokenService tokenService;

    @Value("${app.url}")
    private String appUrl;

    public MailVerificationTokenController(
            MailVerificationTokenService tokenService
    ) {
        this.tokenService = tokenService;
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(
            @RequestBody Map<String, Object> data,
            HttpServletResponse response) {

        String token = data.get("token").toString();
        boolean success = tokenService.verify(token, response);

        if (success) {
            return new ResponseEntity<>("Email verified successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Verification failed or token expired.", HttpStatus.UNAUTHORIZED);
        }
    }
}