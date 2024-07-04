package com.dentalmoovi.website.models.cart;

import java.util.List;

public record CartRequest(
    List<CartDtoRequest> data,
    long idUser
) {}
