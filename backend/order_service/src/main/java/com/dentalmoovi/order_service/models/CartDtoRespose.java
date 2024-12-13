package com.dentalmoovi.order_service.models;

import com.dentalmoovi.order_service.models.dto.ImagesDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartDtoRespose {
    private long id;
    private ImagesDTO image;
    private String productName;
    private double prize;
    private int amount;
    private double subtotal;
    private String prizePDF;
    private String subtotalPDF;
}
