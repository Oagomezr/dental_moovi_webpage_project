package com.dentalmoovi.website.models.dtos;

public record OrderDTO(
    Long idOrder,
    String firstName,
    String lastName,
    String municipaly,
    String departament,
    String address,
    String date
) {}
