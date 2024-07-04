package com.dentalmoovi.website.models.entities;

import org.springframework.data.annotation.Id;

public record Products(
    @Id Long id,
    String name,
    String description,
    String shortDescription,
    double unitPrice,
    int stock,
    boolean openToPublic,
    boolean showPrice,
    Long idMainImage,
    Long idCategory
) {}
