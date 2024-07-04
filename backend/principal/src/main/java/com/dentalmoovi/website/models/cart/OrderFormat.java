package com.dentalmoovi.website.models.cart;

import java.util.List;

public record OrderFormat(
    Long orderNumber,
    String customerName,
    String customerLastName,
    String celPhone,
    String date,
    String hour,
    String departament,
    String location,
    String address,
    String enterprise,
    List<CartDtoRespose> products,
    String total
) {}
