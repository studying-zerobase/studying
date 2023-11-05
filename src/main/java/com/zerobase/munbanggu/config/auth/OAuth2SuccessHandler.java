package com.zerobase.munbanggu.config.auth;

import static com.zerobase.munbanggu.user.exception.ErrorCode.*;

import com.zerobase.munbanggu.user.exception.DuplicatedEmailConflictException;
import com.zerobase.munbanggu.user.model.entity.User;
import com.zerobase.munbanggu.user.repository.UserRepository;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    @Value("${jwt.expiration.refresh-token-seconds}")
    private Long refreshTokenExpirationTimeInSeconds;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        User user = userRepository.findById(oAuth2User.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException(
                        NOT_FOUND_EMAIL.getMessage()));
        if (user.getAuthProvider() == null || !user.getAuthProvider().equals(oAuth2User.getAuthProvider())) {
            throw new DuplicatedEmailConflictException(EMAIL_CONFLICT);
        }

        Long userId = oAuth2User.getUser().getId();
        String authority = "";
        Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();
        if (!authorities.isEmpty()) {
            authority = authorities.iterator().next().getAuthority();
        }
        String accessToken = tokenProvider.generateAccessToken(userId, authority);
        String refreshToken = tokenProvider.generateRefreshToken(userId, authority);

        redisUtil.setData(refreshToken, String.valueOf(oAuth2User.getUser().getId()),
                refreshTokenExpirationTimeInSeconds);

        log.info("redisUtil.getData(): " + redisUtil.getData(refreshToken));

        tokenProvider.addAccessRefreshTokenToResponseHeader(response, accessToken, refreshToken);
    }
}
