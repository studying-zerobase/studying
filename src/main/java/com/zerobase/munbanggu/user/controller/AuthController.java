package com.zerobase.munbanggu.user.controller;

import com.zerobase.munbanggu.user.service.SendMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final SendMailService sendMailService;

    @PostMapping("/email-send") //이메일 발송
    public ResponseEntity<String> sendMail(String email){
        return ResponseEntity.ok(sendMailService.sendMail(email));
    }

    @PostMapping("/email-auth") //이메일 인증
    public ResponseEntity<String> verifyMail(@RequestParam  String email, @RequestParam String code){

        return ResponseEntity.ok(sendMailService.verifyCode(email,code));
    }
}
