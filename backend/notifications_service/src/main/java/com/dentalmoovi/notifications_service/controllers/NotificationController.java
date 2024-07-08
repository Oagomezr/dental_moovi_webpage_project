package com.dentalmoovi.notifications_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dentalmoovi.notifications_service.models.EmailData;
import com.dentalmoovi.notifications_service.services.NotificationSer;

@RestController
@RequestMapping
public class NotificationController {

    private final NotificationSer notificationSer;

    public NotificationController(NotificationSer notificationSer) {
        this.notificationSer = notificationSer;
    }

    @GetMapping("/isCodeCorrect/{email}/{code}")
    public ResponseEntity<Boolean> isCodeCorrect(@PathVariable("email") String email, @PathVariable("code") String code) {
        try{
            return ResponseEntity.ok(notificationSer.isCodeCorrect(email, code));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/sendCode")
    public ResponseEntity<Void> sendEmailCode(@RequestBody EmailData emailData){
        try {
            notificationSer.sendEmailNotificationCode(emailData);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailData emailData){
        try {
            notificationSer.sendEmailNotificationCode(emailData);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
