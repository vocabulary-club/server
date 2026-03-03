package web.server.api.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;
import web.server.api.common.ErrorCode;
import web.server.api.service.MailVerificationTokenService;

import java.io.IOException;
import java.util.HashMap;
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
    public void verify(
            @RequestBody Map<String, Object> data,
            HttpServletResponse response) throws IOException {

        String token = data.get("token").toString();
        boolean success = tokenService.verify(token, response);

        if (success) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ErrorCode error = ErrorCode.MAIL_NOT_VERIFIED;

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("error", error.name());

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), responseBody);
        }
    }
}