package com.zerobase.munbanggu.user.controller;

import com.zerobase.munbanggu.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(
            @RequestHeader(value = "Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println("test");
        return ResponseEntity.ok().body("test");
    }
}
