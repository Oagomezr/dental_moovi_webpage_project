package com.dentalmoovi.website.models.entities;

import org.springframework.data.annotation.Id;

import com.dentalmoovi.website.models.entities.enums.RolesList;

public record Roles(
    @Id Long id,
    RolesList role
) {}

