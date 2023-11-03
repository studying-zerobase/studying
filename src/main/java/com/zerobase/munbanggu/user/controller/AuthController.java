package com.zerobase.munbanggu.user.controller;

import com.zerobase.munbanggu.user.service.SendMailService;
import com.zerobase.munbanggu.user.service.SendMessageService;
import com.zerobase.munbanggu.user.type.AuthenticationStatus;
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
public class AuthController {
    private final SendMailService sendMailService;
    private final SendMessageService sendMessageService;

    @PostMapping("/email-send") //이메일 발송
    public ResponseEntity<AuthenticationStatus> sendMail(String email){
        return ResponseEntity.ok(sendMailService.sendMail(email));
    }

    @PostMapping("/email-auth") //이메일 인증
    public ResponseEntity<AuthenticationStatus> verifyMail(String email,String code){

        return ResponseEntity.ok(sendMailService.verifyCode(email,code));
    }

    @PostMapping("/phone-send") // 핸드폰 인증번호 발송
    public ResponseEntity<AuthenticationStatus> sendSMS(String phoneNumber){
        return ResponseEntity.ok(sendMessageService.verifyPhoneNumber(phoneNumber));
    }

    @PostMapping("/phone-auth") // 핸드폰 인증
    public ResponseEntity<AuthenticationStatus> verifySMS(String phoneNumber, String code){
        return ResponseEntity.ok(sendMessageService.verifyCode(phoneNumber,code));
    }

}
