package com.zerobase.munbanggu.user.controller;

import com.zerobase.munbanggu.dto.SignInDto;
import com.zerobase.munbanggu.user.service.UserService;
import com.zerobase.munbanggu.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;

    private static final String AUTH_HEADER = "Authorization";

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody SignInDto signInDto){
        System.out.println(signInDto);
        return ResponseEntity.ok(userService.signIn(signInDto));
    }


    @PostMapping("/sign-out")
    public ResponseEntity<String> logOut( @RequestHeader(name = AUTH_HEADER) String token){

        if (jwtService.isBlacklisted(token)) {
            return ResponseEntity.ok("이미 로그아웃된 토큰입니다.");
        }
        jwtService.logout(token);
        return ResponseEntity.ok("로그아웃");
    }

}
