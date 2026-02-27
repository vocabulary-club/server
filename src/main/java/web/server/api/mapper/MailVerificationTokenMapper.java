package web.server.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import web.server.api.entity.MailVerificationTokenEntity;
import web.server.api.entity.TokenEntity;

import java.time.Instant;

@Mapper
public interface MailVerificationTokenMapper {

    MailVerificationTokenEntity selectByToken(String token);

    int insert(MailVerificationTokenEntity entity);

    int deleteExpiredTokens(Instant expiration);

    int deleteByToken(String token);
}