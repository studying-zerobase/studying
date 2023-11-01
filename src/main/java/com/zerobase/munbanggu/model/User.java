package com.zerobase.munbanggu.model;

import com.zerobase.munbanggu.type.AuthType;
import com.zerobase.munbanggu.type.UserType;
import java.time.LocalDateTime;
import javax.lang.model.element.Name;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @Column(name="nick_name")
    private String nickName;

    private String name;

    private String category;

    private String phone;

    private String email;

    private String gender;

    private String area;

    private String photo;

    @CreatedDate
    private LocalDateTime createTime;

    private int age;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    private AuthType authType;
    @Column(name="failed_count")
    @Default
    private int failedCount=0;

}
