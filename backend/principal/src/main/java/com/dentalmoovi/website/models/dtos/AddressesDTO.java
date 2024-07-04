package com.dentalmoovi.website.models.dtos;

public record AddressesDTO(
    long id,
    String address,
    String phone,
    String description,
    String location,
    String departament,
    Integer idMunicipaly
) {
}
