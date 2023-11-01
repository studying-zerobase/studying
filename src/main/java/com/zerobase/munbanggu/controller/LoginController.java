package com.zerobase.munbanggu.controller;

import com.zerobase.munbanggu.dto.SignInDto;
import com.zerobase.munbanggu.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("auth/sign-in")
    public ResponseEntity<String> signIn(@RequestBody SignInDto signInDto){
        System.out.println(signInDto);
        return ResponseEntity.ok(loginService.signIn(signInDto));
    }

}
