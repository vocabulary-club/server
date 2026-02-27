package web.server.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import web.server.api.entity.MailVerificationTokenEntity;
import web.server.api.mapper.MailVerificationTokenMapper;

import java.time.Instant;

@Service
public class MailVerificationTokenService {

    private static final Logger log = LoggerFactory.getLogger(MailVerificationTokenService.class);

    private final MailVerificationTokenMapper tokenMapper;

    public MailVerificationTokenService(MailVerificationTokenMapper tokenMapper) {

        this.tokenMapper = tokenMapper;
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
}