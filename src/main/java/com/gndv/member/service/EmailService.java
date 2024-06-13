package com.gndv.member.service;

import com.gndv.configs.EmailConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailConfig emailConfig;
    private final RedisTemplate<String, Object> redisTemplate;

    public boolean sendEmail(String email) {
        try {
            String rand = emailConfig.createNumber();
            System.out.println(email);
            redisTemplate.opsForValue().set(email, rand, 3, TimeUnit.MINUTES);
            MimeMessage message = emailConfig.createEmailForm(email, rand);
            javaMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email: " + e.getMessage());
        }
        return false;
    }

    public boolean verifyCode(String email, String code) {
        try {
            String storedCode = (String) redisTemplate.opsForValue().get(email);
            System.out.println("Stored code: " + storedCode);
            return storedCode != null && storedCode.equals(code);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to verify code: " + e.getMessage());
        }
        return false;
    }
}
