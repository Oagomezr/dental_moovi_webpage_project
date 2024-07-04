package com.dentalmoovi.website.models.dtos;

public record ImagesDTO(
    long id,
    String name,
    String contentType,
    String imageBase64
) {}
