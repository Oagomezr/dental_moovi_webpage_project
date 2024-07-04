package com.dentalmoovi.website.models.entities.many_to_many;

import org.springframework.data.relational.core.mapping.Table;

@Table("orders_products")
public record OrdersProducts(
    Long idOrder,
    Long idProduct,
    int amount
){}
