package com.zerobase.munbanggu.user.service;


import static com.zerobase.munbanggu.type.ErrorCode.EMAIL_NOT_EXISTS;
import static com.zerobase.munbanggu.type.ErrorCode.USER_NOT_EXIST;
import static com.zerobase.munbanggu.type.ErrorCode.USER_WITHDRAWN;
import static com.zerobase.munbanggu.type.ErrorCode.WRONG_PASSWORD;
import static com.zerobase.munbanggu.user.type.Role.INACTIVE;

import com.zerobase.munbanggu.config.auth.TokenProvider;
import com.zerobase.munbanggu.dto.TokenResponse;
import com.zerobase.munbanggu.user.dto.GetUserDto;
import com.zerobase.munbanggu.user.dto.SignInDto;
import com.zerobase.munbanggu.user.dto.UserRegisterDto;
import com.zerobase.munbanggu.user.exception.InvalidNicknameException;
import com.zerobase.munbanggu.user.exception.NicknameAlreadyExistsException;
import com.zerobase.munbanggu.user.exception.UserException;
import com.zerobase.munbanggu.user.model.entity.User;
import com.zerobase.munbanggu.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    public TokenResponse signIn(SignInDto signInDto) {
        User user = userRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(() -> new UserException(USER_NOT_EXIST));

        // 비밀번호 체크
        boolean isMatch = passwordEncoder.matches(signInDto.getPassword(), user.getPassword());
        if (!isMatch) {
            throw new UserException(WRONG_PASSWORD);
        }
        if (user.getRole().equals(INACTIVE)) {
            throw new UserException(USER_WITHDRAWN);
        }

        //return "로그인 완료";
        String accessToken = tokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = tokenProvider.generateRefreshToken(user.getId(), user.getEmail(), user.getRole());

        tokenProvider.saveRefreshTokenInRedis(user.getEmail(), refreshToken);

        return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public GetUserDto updateUser(Long id, GetUserDto getUserDto) { //유저정보 업데이트
        // 해당하는 유저가 존재하지 않을경우
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException(USER_NOT_EXIST));

        user.setNickname(getUserDto.getNickname());
        user.setEmail(getUserDto.getEmail());
        user.setPhone(getUserDto.getPhone());
        user.setProfileImageUrl(getUserDto.getProfileImageUrl());
        userRepository.save(user);
        return getUserDto;
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public GetUserDto getInfo(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(EMAIL_NOT_EXISTS));

        return GetUserDto.builder().
                email(user.getEmail())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }

    public void registerUser(UserRegisterDto userDto) {
        // 닉네임 유효성 검사
        if (!userDto.getNickname().matches("[가-힣a-zA-Z0-9]{2,10}")) {
            throw new InvalidNicknameException("Invalid nickname format");
        }

        // 닉네임 중복 확인
        userRepository.findByNickname(userDto.getNickname())
                .ifPresent(u -> {
                    throw new NicknameAlreadyExistsException("Nickname already exists");
                });

        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(encodedPassword)
                .nickname(userDto.getNickname())
                .phone(userDto.getPhone())
                .profileImageUrl(userDto.getProfileImageUrl())
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void updateProfileImage(Long userId, String imageUrl) {
        User siteUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        siteUser.setProfileImageUrl(imageUrl);
        userRepository.save(siteUser);
    }

    public String getProfileUrl(Long userId) {
        User siteUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        return siteUser.getProfileImageUrl();
    }
}
