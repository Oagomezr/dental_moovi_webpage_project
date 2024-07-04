package com.dentalmoovi.website.models.dtos;

import java.time.LocalDate;

import com.dentalmoovi.website.models.entities.enums.GenderList;

public record UserDTO(
    Long idUser,
    String firstName,
    String lastName,
    String email,
    String celPhone,
    LocalDate birthdate,
    GenderList gender,
    String code, 
    String password
) {}

