package com.dentalmoovi.website.models.responses;

import java.util.List;

import com.dentalmoovi.website.models.dtos.ProductsDTO;

public record ProductsResponse(
    int totalProducts,
    int paginatedProducts,
    List<ProductsDTO> data
) {}

