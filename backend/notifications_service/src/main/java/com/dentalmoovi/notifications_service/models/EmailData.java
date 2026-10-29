package com.dentalmoovi.notifications_service.models;

import java.util.List;

public record EmailData(
    List<String> to,
    String subject,
    String body,
    String whoSend
) {} 

