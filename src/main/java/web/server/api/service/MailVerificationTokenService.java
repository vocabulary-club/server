package web.server.api.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import web.server.api.entity.MailVerificationTokenEntity;
import web.server.api.entity.TokenEntity;
import web.server.api.jwt.JwtUtil;
import web.server.api.mapper.MailVerificationTokenMapper;
import web.server.api.mapper.UserMapper;

import java.io.IOException;
import java.time.Instant;

@Service
public class MailVerificationTokenService {

    private static final Logger log = LoggerFactory.getLogger(MailVerificationTokenService.class);

    private final MailVerificationTokenMapper tokenMapper;
    private final UserMapper userMapper;

    private final JwtUtil jwtUtil;

    private final TokenService tokenService;
    private final SecretService secretService;

    public MailVerificationTokenService(
            MailVerificationTokenMapper tokenMapper,
            UserMapper userMapper,
            JwtUtil jwtUtil,
            TokenService tokenService,
            SecretService secretService) {

        this.tokenMapper = tokenMapper;
        this.userMapper = userMapper;

        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
        this.secretService = secretService;
    }

    public MailVerificationTokenEntity selectByToken(String token) {
        return tokenMapper.selectByToken(token);
    }

    public int insert(MailVerificationTokenEntity entity) {
        return tokenMapper.insert(entity);
    }

    @Scheduled(fixedRate = 180000)
    public void deleteExpiredTokens() {
    	Instant expiration = Instant.now();
        log.info("DB mail verification token check time: " + expiration);
        tokenMapper.deleteExpiredTokens(expiration);
    }

    public int deleteByToken(String token) {
        return tokenMapper.deleteByToken(token);
    }

    public boolean verify(
            String token,
            HttpServletResponse response) {

        MailVerificationTokenEntity mailVerificationTokenEntity = tokenMapper.selectByToken(token);

        if (mailVerificationTokenEntity == null) {
            return false;
        }

        String username = mailVerificationTokenEntity.getUsername();

        tokenMapper.deleteByUsername(username);

        userMapper.verify(username);

        log.info(username + " mail is verified");

        String role = "ROLE_USER";

        String newAccessToken = jwtUtil.createJwt("access", username, role, secretService.getJwtAccess());
        String newRefreshToken = jwtUtil.createJwt("refresh", username, role, secretService.getJwtRefresh());

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUsername(username);
        tokenEntity.setToken(newRefreshToken);
        tokenEntity.setExpiration(Instant.now().plusMillis(secretService.getJwtRefresh()));
        tokenService.insert(tokenEntity);

        response.setHeader("Authorization", "Bearer " + newAccessToken);

        // create client refresh-token
        Cookie cookie = new Cookie("refresh", newRefreshToken);
        cookie.setMaxAge(secretService.getJwtRefreshCookie());
        cookie.setSecure(true);	// use case is https
        cookie.setPath("/");		// Бүх эндпойнт дээр илгээгдэх
        cookie.setHttpOnly(true);	// cannot use cookie in java script
        response.addCookie(cookie);

        log.info("tokens are created successfully");

        return true;
    }
}