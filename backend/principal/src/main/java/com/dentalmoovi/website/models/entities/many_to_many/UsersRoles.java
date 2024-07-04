package com.dentalmoovi.website.models.entities.many_to_many;

import org.springframework.data.relational.core.mapping.Table;

@Table("users_roles")
public record UsersRoles(
    Long idUser,
    Long idRole
) {}
