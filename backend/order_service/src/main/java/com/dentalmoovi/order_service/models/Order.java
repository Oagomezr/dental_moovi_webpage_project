package com.dentalmoovi.order_service.models;

public record Order(
    Long id,
    byte[] orderFile
) {
}
