package com.zerobase.munbanggu.config.auth;

import com.zerobase.munbanggu.user.model.entity.User;
import java.io.Serializable;

public class SessionUser implements Serializable {

    private String name;
    private String email;
    private String profile;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.profile = user.getProfileImageUrl();
    }

}
