package com.dentalmoovi.website.models.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

public record ActivityLogs(
    @Id Long id,
    String description,
    LocalDateTime date,
    Long idUser
) {}
