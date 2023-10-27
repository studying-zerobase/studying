package com.zerobase.munbanggu.config.auth;

import com.zerobase.munbanggu.model.entity.User;
import com.zerobase.munbanggu.type.AuthProvider;
import com.zerobase.munbanggu.type.Role;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String profile;
    private Role role;
    private AuthProvider authProvider;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email,
            String profile, Role role, AuthProvider authProvider) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.role = role;
        this.authProvider = authProvider;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName,
            Map<String, Object> attributes) {
        if (registrationId.equals("Kakao")) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofKakao(userNameAttributeName, attributes);

    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        log.info("attributes: " + attributes);
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) attributes.get("properties");
        log.info("kakaoAccount: " + kakaoAccount);
        log.info("kakaoProfile: " + kakaoProfile);
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .profile((String) kakaoProfile.get("profile_image"))
                .authProvider(AuthProvider.KAKAO)
                .role(Role.USER)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .profile(profile)
                .role(role)
                .authProvider(authProvider)
                .build();
    }

}
