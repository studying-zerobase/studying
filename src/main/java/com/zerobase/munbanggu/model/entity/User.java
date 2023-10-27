package com.zerobase.munbanggu.model.entity;

import com.zerobase.munbanggu.type.AuthProvider;
import com.zerobase.munbanggu.type.Role;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String profile;

    @Enumerated(value = EnumType.STRING)
    private AuthProvider authProvider;

    @Builder
    public User(String name, String email, String profile, Role role, AuthProvider authProvider) {
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.role = role;
        this.authProvider = authProvider;
    }

    public User update(String name, String profile) {
        this.name = name;
        this.profile = profile;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
