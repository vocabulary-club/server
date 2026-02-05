package web.server.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import web.server.api.entity.TokenEntity;
import web.server.api.mapper.TokenMapper;

import java.time.Instant;

@Service
public class TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    private final TokenMapper tokenMapper;

    public TokenService(TokenMapper tokenMapper) {

        this.tokenMapper = tokenMapper;
    }

    public TokenEntity selectByToken(String token) {
        return tokenMapper.selectByToken(token);
    }

    public int insert(TokenEntity entity) {
        return tokenMapper.insert(entity);
    }

    @Scheduled(fixedRate = 60000)
    public void deleteExpiredTokens() {
    	Instant expiration = Instant.now();
        log.info("DB token check time: " + expiration);
        tokenMapper.deleteExpiredTokens(expiration);
    }

    public int deleteByToken(String token) {
        return tokenMapper.deleteByToken(token);
    }

    // one username must have only one refresh token
    public int deleteByUsername(String username) {
        return tokenMapper.deleteByUsername(username);
    }
}