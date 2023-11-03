package com.zerobase.munbanggu.user.service;

import static com.zerobase.munbanggu.type.ErrorCode.USER_NOT_EXIST;
import static com.zerobase.munbanggu.type.ErrorCode.WRONG_PASSWORD;

import com.zerobase.munbanggu.dto.SignInDto;
import com.zerobase.munbanggu.user.exception.LoginException;
import com.zerobase.munbanggu.util.JwtService;
import com.zerobase.munbanggu.user.model.entity.User;
import com.zerobase.munbanggu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    private final JwtService jwtService;

    public String signIn(SignInDto signInDto) {
        User user = userRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(()-> new LoginException(USER_NOT_EXIST));

        // 비밀번호 체크
        if(!signInDto.getPassword().equals(user.getPassword())){
            throw new LoginException(WRONG_PASSWORD);

        }

        //return "로그인 완료";
        return jwtService.createToken(user.getId(), user.getEmail());
    }
}
