package com.dentalmoovi.order_service.models;

public record CartDtoRespose(
    long id,
    ImagesDTO image,
    String productName,
    double prize,
    int amount,
    double subtotal,
    String prizePDF,
    String subtotalPDF
) {}
