package com.dentalmoovi.notifications_service.services;

import java.io.UnsupportedEncodingException;
import java.util.List;

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

    public void sendEmail(List<String> toList, String subject, String body, String whoSend) throws UnsupportedEncodingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("soporte@soltelec", whoSend);
            helper.setTo(toList.toArray(new String[0])); // múltiples destinatarios
            helper.setSubject(subject);
            helper.setText(body, true); // 'true' para contenido HTML

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}