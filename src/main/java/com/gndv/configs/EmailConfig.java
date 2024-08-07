package com.gndv.configs;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class EmailConfig {

    private final JavaMailSender javaMailSender;

    public String createNumber() {

        int length = 6;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rand = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = rand.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public MimeMessage createEmailForm(String email, String randomNumber) throws MessagingException {

        String subject = "인증번호 발송";
        String content = "귀하의 인증번호는 다음과 같습니다: " + randomNumber;

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        System.out.println("form:" + email);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);

        return message;
    }
}
