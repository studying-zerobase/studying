package com.zerobase.munbanggu.service;

import static com.zerobase.munbanggu.type.ErrorCode.USER_NOT_EXIST;
import static com.zerobase.munbanggu.type.ErrorCode.WRONG_PASSWORD;

import com.zerobase.munbanggu.dto.SignInDto;
import com.zerobase.munbanggu.exception.LoginException;
import com.zerobase.munbanggu.model.User;
import com.zerobase.munbanggu.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final LoginRepository loginRepository;

    private final JwtService jwtService;

    public String signIn(SignInDto signInDto) {
        User user = loginRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(()-> new LoginException(USER_NOT_EXIST));

        // 비밀번호 체크
        if(!signInDto.getPassword().equals(user.getPassword())){
            throw new LoginException(WRONG_PASSWORD);

        }

        //return "로그인 완료";
        return jwtService.createToken(user.getId(), user.getEmail());
    }
}
