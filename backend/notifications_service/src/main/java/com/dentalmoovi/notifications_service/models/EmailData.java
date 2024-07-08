package com.dentalmoovi.notifications_service.models;

public record EmailData(
    String to,
    String subject,
    String body
) {} 

