package com.dentalmoovi.order_service.models;

public record ImagesDTO(
    long id,
    String name,
    String contentType,
    String imageBase64
) {}
