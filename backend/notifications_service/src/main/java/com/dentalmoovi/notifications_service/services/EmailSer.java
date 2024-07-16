package com.dentalmoovi.notifications_service.services;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSer {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;
    
    EmailSer(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String body) throws UnsupportedEncodingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("notificaciones@dentalmoovi.com", "Dental Moovi");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // 'true' for HTML content

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // handle the exception
        }
    }
}