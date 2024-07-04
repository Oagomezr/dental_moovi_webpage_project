package com.dentalmoovi.website.models.cart;

public record CartDtoRequest(
    long id,
    double prize,
    int amount
) {}
