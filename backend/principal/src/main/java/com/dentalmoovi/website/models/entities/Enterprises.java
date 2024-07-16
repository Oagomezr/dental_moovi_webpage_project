package com.dentalmoovi.website.models.entities;

import org.springframework.data.annotation.Id;

public record Enterprises(
    @Id Long id,
    String name
) {
}
