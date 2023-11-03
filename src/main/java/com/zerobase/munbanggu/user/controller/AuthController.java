package com.zerobase.munbanggu.user.controller;

import com.zerobase.munbanggu.dto.SignInDto;
import com.zerobase.munbanggu.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("sign-in")
    public ResponseEntity<String> signIn(@RequestBody SignInDto signInDto){
        System.out.println(signInDto);
        return ResponseEntity.ok(userService.signIn(signInDto));
    }


    @PostMapping("sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignInDto signInDto){
        System.out.println(signInDto);
        return ResponseEntity.ok(userService.signIn(signInDto));
    }

}
