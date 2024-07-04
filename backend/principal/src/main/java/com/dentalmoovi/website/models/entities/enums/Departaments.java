package com.dentalmoovi.website.models.entities.enums;

import org.springframework.data.annotation.Id;

public record Departaments(
    @Id Integer id,
    String name
) {}
