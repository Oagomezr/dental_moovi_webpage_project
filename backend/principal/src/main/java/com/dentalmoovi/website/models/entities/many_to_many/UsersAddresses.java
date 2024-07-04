package com.dentalmoovi.website.models.entities.many_to_many;

import org.springframework.data.relational.core.mapping.Table;

@Table("users_addresses")
public record UsersAddresses(
    Long idUser,
    Long idAddress
) {}
