package com.zerobase.munbanggu.user.service;
import com.zerobase.munbanggu.user.type.AuthenticationStatus;
import org.apache.commons.lang3.RandomStringUtils;


import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Component
public class SendMailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    @Value("${spring.mail.username}")
    private String id;

    @Value("${secret.recipient}")
    private String recipient;

    private String keyGenerator(){
        return RandomStringUtils.random(10,true,true);
    }

    private MimeMessage createMessage(String code, String email) throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("[문방구] 이메일 인증 확인");
        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">"
                + "안녕하세요, <br>커뮤니티 플랫폼 \"문방구\" 입니다.<br></h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">"
                + "<br>이메일 인증을 위해 아래 코드를 회원가입 화면에 입력해주세요.</p>";
        msg += "<div style=\"padding: 0 30px; margin: 15px 0 40px;\">"
                + "<table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; "
                + "height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\">"
                + "<tbody><tr><td style=\"padding : 0px 20px; text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += code;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress(id,"munbanggu"));

        return  message;
    }

    public AuthenticationStatus sendMail(String email){
        String code = keyGenerator();
        log.info("\n>>>>>> recipient: " + recipient);
        log.info("\n>>>>>> code: " + code);
        try {
            MimeMessage mimeMessage = createMessage(code, email);
            javaMailSender.send(mimeMessage);
            redisUtil.setData(email, code, 5);
            return AuthenticationStatus.SUCCESS;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AuthenticationStatus verifyCode(String email, String input) {
        String code = "";
        try {
            code = redisUtil.getData(email);
            log.info("\ncode : " + code + " input_code : " + input + " same?: "
                    + code.equals(input));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        if (code.equals(input)) {
            return AuthenticationStatus.SUCCESS;
        }
        return AuthenticationStatus.FAIL;

    }
}

