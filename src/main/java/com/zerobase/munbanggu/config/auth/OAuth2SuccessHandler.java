package com.zerobase.munbanggu.config.auth;

import com.zerobase.munbanggu.user.service.RedisUtil;
import java.io.IOException;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;

    @Value("${jwt.expiration.refresh-token-seconds}")
    private Long refreshTokenExpirationTimeInSeconds;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String username = oAuth2User.getUser().getEmail();
        String authority = "";
        Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();
        if (!authorities.isEmpty()) {
            authority = authorities.iterator().next().getAuthority();
        }
        String accessToken = tokenProvider.generateAccessToken(username, authority);
        String refreshToken = tokenProvider.generateRefreshToken(username, authority);

        redisUtil.setData(String.valueOf(oAuth2User.getUser().getId()), refreshToken,
                refreshTokenExpirationTimeInSeconds);

        log.info("redisUtil.getData(): "+redisUtil.getData(String.valueOf(oAuth2User.getUser().getId())));

        tokenProvider.addAccessRefreshTokenToResponseHeader(response, accessToken, refreshToken);
    }
}
