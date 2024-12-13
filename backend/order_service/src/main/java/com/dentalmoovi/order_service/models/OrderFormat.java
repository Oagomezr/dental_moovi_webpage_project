package com.dentalmoovi.order_service.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderFormat {
    private Long orderNumber;
    private String customerName;
    private String customerLastName;
    private String celPhone;
    private String date;
    private String hour;
    private String departament;
    private String location;
    private String address;
    private String enterprise;
    private List<CartDtoRespose> products;
    private String total;
}
