package com.dentalmoovi.website.models.entities;

import org.springframework.data.annotation.Id;

import jakarta.annotation.Nonnull;

public record Addresses(
    @Id Long id,
    String address,
    String phone,
    String description,
    @Nonnull Integer idMunicipalyCity
) {}
