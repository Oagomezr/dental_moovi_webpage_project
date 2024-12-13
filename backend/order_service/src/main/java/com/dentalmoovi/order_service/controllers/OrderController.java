package com.dentalmoovi.order_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.dentalmoovi.order_service.models.OrderFormat;
import com.dentalmoovi.order_service.services.OrderSer;

@Controller
@RequestMapping
public class OrderController {
    private final OrderSer orderSer;

    @PostMapping("/generate")
    public ResponseEntity<Void> generatePDF(@RequestBody OrderFormat orderData){
        try {
            orderSer.generateOrder(orderData);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public OrderController(OrderSer orderSer) {
        this.orderSer = orderSer;
    }
}
