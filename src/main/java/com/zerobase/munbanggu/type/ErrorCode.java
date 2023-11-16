package com.zerobase.munbanggu.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_EXIST("해당 계정은 존재하지 않습니다."),
    USER_WITHDRAWN("해당 계정은 탈퇴된 계정입니다."),
    WRONG_PASSWORD("비밀번호가 일치하지 않습니다."),
    EMAIL_CONFLICT("이미 가입된 이메일 입니다. 다른 방법으로 로그인해 주세요."),
    INVALID_TOKEN("토큰이 유효하지 않습니다."),
    EMAIL_NOT_EXISTS("가입되지 않은 이메일입니다."),
    INVALID_EMAIL("이메일이 일치하지 않습니다."),
    INVALID_CODE("인증번호가 일치하지 않습니다."),
    INVALID_PHONE("핸드폰번호가 일치하지 않습니다."),
    STUDY_NOT_EXISTS("스터디가 존재하지 않습니다."),
    CHECKLIST_NOT_EXISTS("일치하는 체크리스트가 존재하지 않습니다"),
    TOKEN_UNMATCHED("아이디와 토큰정보가 일치하지 않습니다"),
    ;
    private String description;
}
