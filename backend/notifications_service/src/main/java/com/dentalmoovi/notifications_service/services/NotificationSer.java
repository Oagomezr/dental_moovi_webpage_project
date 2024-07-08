package com.dentalmoovi.notifications_service.services;

import org.springframework.stereotype.Service;

import com.dentalmoovi.notifications_service.Utils;
import com.dentalmoovi.notifications_service.models.EmailData;
import com.dentalmoovi.notifications_service.services.cache.CacheSer;

@Service
public class NotificationSer {
    private final EmailSer emailSer;
    private final CacheSer cacheSer;

    public NotificationSer(EmailSer emailSer, CacheSer cacheSer) {
        this.emailSer = emailSer;
        this.cacheSer = cacheSer;
    }

    public void sendEmailNotificationCode(EmailData data){
        String retrictReplay = cacheSer.getFromReplayCodeRestrict(data.to());
        if (retrictReplay !=null && retrictReplay.equals(data.to())) return;

        String formattedNumber = Utils.generateRandom6Number();

        String body = data.body();

        body += formattedNumber;
        
        cacheSer.addToOrUpdateRegistrationCache(data.to(), formattedNumber);
        cacheSer.addToOrUpdateReplayCodeRestrict(data.to(), data.to());

        try {
            emailSer.sendEmail(data.to(), data.subject(), body);
        } catch (Exception e) {
            // Manage the exception in case it cannot send the email
            e.printStackTrace();
        }
    }

    public boolean isCodeCorrect(String email, String codeEntered){
        //get verification code
        String code = cacheSer.getFromRegistrationCache(email);
        boolean isCorrect = codeEntered.equals(code);
        if (isCorrect) cacheSer.removeFromRegistrationCache(email);
        return isCorrect;
    }

    public void sendEmail(EmailData data){
        try {
            emailSer.sendEmail(data.to(), data.subject(), data.body());
        } catch (Exception e) {
            // Manage the exception in case it cannot send the email
            e.printStackTrace();
        }
    }

}
