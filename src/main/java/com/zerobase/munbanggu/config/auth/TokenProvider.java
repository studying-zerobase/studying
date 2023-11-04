package com.zerobase.munbanggu.config.auth;

import static com.zerobase.munbanggu.user.exception.ErrorCode.INVALID_TOKEN;
import static com.zerobase.munbanggu.user.exception.ErrorCode.NOT_FOUND_EMAIL;

import com.zerobase.munbanggu.user.model.entity.RefreshToken;
import com.zerobase.munbanggu.user.model.entity.User;
import com.zerobase.munbanggu.user.repository.RefreshTokenRepository;
import com.zerobase.munbanggu.user.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    @PersistenceContext
    private EntityManager entityManager;

    private Key key;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.access-token-seconds}")
    private Long accessTokenExpirationTimeInSeconds;

    @Value("${jwt.expiration.refresh-token-seconds}")
    private Long refreshTokenExpirationTimeInSeconds;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private static final String AUTHORIZATION_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN_KEY = "Access-Token";
    public static final String REFRESH_TOKEN_KEY = "Refresh-Token";
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }


    public String generateAccessToken(String username, String authority) {
        Date expiration = new Date(new Date().getTime() + accessTokenExpirationTimeInSeconds);
        return generateToken(username, authority, expiration);
    }

    @Transactional
    public String generateRefreshToken(String username, String authority) {
        Date expiration = new Date(new Date().getTime() + refreshTokenExpirationTimeInSeconds);
        String token = generateToken(username, authority, expiration);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND_EMAIL.getMessage()));
        user = entityManager.merge(user);
        refreshTokenRepository.save(RefreshToken.builder()
                .token(token)
                .user(user)
                .build());
        return token;
    }

    private String generateToken(String username, String authority, Date expiration) {
        return Jwts.builder()
                .subject(username)
                .claims(getClaims(authority))
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public void addAccessTokenToResponseHeader(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(ACCESS_TOKEN_KEY, AUTHORIZATION_PREFIX + accessToken);
    }

    public void addAccessRefreshTokenToResponseHeader(HttpServletResponse response, String accessToken,
            String refreshToken) {

        response.addHeader(ACCESS_TOKEN_KEY, AUTHORIZATION_PREFIX + accessToken);
        response.addHeader(REFRESH_TOKEN_KEY, AUTHORIZATION_PREFIX + refreshToken);
    }


    public boolean validateToken(String token, String username) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        try {
            String subject = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                    .getSubject();
            Date expiration = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                    .getExpiration();
            Date now = new Date();
            return username.equals(subject) && expiration.after(now);
        } catch (JwtException e) {
            throw new JwtException(INVALID_TOKEN.getMessage());
        }
    }

    private Map<String, String> getClaims(String authority) {
        Map<String, String> claims = new HashMap<>();
        claims.put("authority", authority);
        return claims;
    }

}
