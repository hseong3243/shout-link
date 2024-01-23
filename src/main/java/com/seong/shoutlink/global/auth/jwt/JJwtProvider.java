package com.seong.shoutlink.global.auth.jwt;

import com.seong.shoutlink.domain.auth.JwtProvider;
import com.seong.shoutlink.domain.auth.service.response.ClaimsResponse;
import com.seong.shoutlink.domain.auth.service.response.TokenResponse;
import com.seong.shoutlink.domain.member.MemberRole;
import com.seong.shoutlink.global.exception.ErrorCode;
import com.seong.shoutlink.global.exception.ShoutLinkException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JJwtProvider implements JwtProvider {

    private static final String ROLE = "role";

    private final String issuer;
    private final int expirySeconds;
    private final int refreshExpirySeconds;
    private final SecretKey secretKey;
    private final SecretKey refreshSecretKey;
    private final JwtParser accessTokenParser;
    private final JwtParser refreshTokenParser;

    public JJwtProvider(
        String issuer,
        int expirySeconds,
        int refreshExpirySeconds,
        String secret,
        String refreshSecret) {
        this.issuer = issuer;
        this.expirySeconds = expirySeconds;
        this.refreshExpirySeconds = refreshExpirySeconds;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenParser = Jwts.parser()
            .verifyWith(secretKey)
            .build();
        this.refreshTokenParser = Jwts.parser()
            .verifyWith(refreshSecretKey)
            .build();
    }

    @Override
    public TokenResponse createToken(Long memberId, MemberRole memberRole) {
        String accessToken = createAccessToken(memberId, memberRole);
        String refreshToken = createRefreshToken(memberId, memberRole);
        return new TokenResponse(accessToken, refreshToken);
    }

    private String createAccessToken(Long userId, MemberRole memberRole) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + expirySeconds * 1000L);
        return Jwts.builder()
            .issuer(issuer)
            .issuedAt(now)
            .subject(userId.toString())
            .expiration(expiresAt)
            .claim(ROLE, memberRole.getValue())
            .signWith(secretKey)
            .compact();
    }

    private String createRefreshToken(Long userId, MemberRole memberRole) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + refreshExpirySeconds * 1000L);
        return Jwts.builder()
            .issuer(issuer)
            .issuedAt(now)
            .subject(userId.toString())
            .expiration(expiresAt)
            .claim(ROLE, memberRole.getValue())
            .signWith(refreshSecretKey)
            .compact();
    }

    @Override
    public ClaimsResponse parseAccessToken(String accessToken) {
        try {
            Claims claims = accessTokenParser.parseSignedClaims(accessToken).getPayload();
            Long memberId = Long.valueOf(claims.getSubject());
            MemberRole memberRole = MemberRole.valueOf(claims.get(ROLE, String.class));
            List<String> authorities = memberRole.getAuthorities();
            return new ClaimsResponse(memberId, memberRole, authorities);
        } catch (ExpiredJwtException e) {
            throw new ShoutLinkException("만료된 액세스 토큰입니다.", ErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (JwtException e) {
            log.debug("[Ex] {} 유효하지 않은 액세스 토큰입니다.", e.getClass().getSimpleName());
        } catch (RuntimeException e) {
            log.debug("[Ex] {} 유효하지 않은 인자가 전달되었습니다.", e.getClass().getSimpleName());
        }
        throw new ShoutLinkException("유요하지 않은 액세스 토큰입니다.", ErrorCode.INVALID_ACCESS_TOKEN);
    }
}
