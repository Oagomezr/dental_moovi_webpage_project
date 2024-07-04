package com.dentalmoovi.website.models.entities;

import org.springframework.data.annotation.Id;

public record Categories(
    @Id Long id,
    String name,
    Long idParentCategory
) {}
