package com.zerobase.munbanggu.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SendMessageServiceTest {
    @Value("${secret.phone-number}")
    private String phoneNumber;

    @Autowired
    SendMessageService sendMessageService;
    @Test
    public void sendMsgTest(){
        sendMessageService.verifyPhoneNumber(phoneNumber);
    }

    @Test
    public void verifyCodeTest(){
        String code = "56261";
        assert (sendMessageService.verifyCode(phoneNumber,code).equals("인증성공"));
    }

}