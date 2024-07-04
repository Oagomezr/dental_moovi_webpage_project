package com.dentalmoovi.website.models.cart;

import com.dentalmoovi.website.models.dtos.ImagesDTO;

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
