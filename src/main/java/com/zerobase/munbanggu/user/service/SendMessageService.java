package com.zerobase.munbanggu.user.service;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor

public class SendMessageService {
    private final RedisUtil redisUtil;

    @Value("${coolsms.api-key}")
    private String apiKey;

    @Value("${coolsms.secret-key}")
    private String secretKey;

    @Value("${secret.phone-number}")
    private String senderPhoneNumber;


    private String keyGenerator(){
        return RandomStringUtils.random(5,false,true);
    }

    public String verifyPhoneNumber(String phoneNumber) {
        String key = keyGenerator();

        Message coolsms = new Message(apiKey, secretKey);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);
        params.put("from", senderPhoneNumber);
        params.put("type", "SMS");
        params.put("text", "[문방구] 핸드폰 인증 메세지\n 인증번호는 ["+key+"] 입니다.");
        params.put("app_version", "test app 1.2"); // application name and version

        log.info("\n"+params.toString());
        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            redisUtil.setData(phoneNumber,key,2);  //redis에 2분간 저장

        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }

        return "인증번호 전송 완료";
    }

    public String verifyCode(String phoneNumber, String input) {
        String code = redisUtil.getData(phoneNumber);

        if (code.equals(input)) {
            return "인증성공";
        }
        return "인증실패";
    }

}
