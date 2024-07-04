package com.dentalmoovi.website.models.cart;

import java.util.List;

public record CartResponse(
    List<CartDtoRespose> data,
    double total,
    int amountOfProducts
) {
}
