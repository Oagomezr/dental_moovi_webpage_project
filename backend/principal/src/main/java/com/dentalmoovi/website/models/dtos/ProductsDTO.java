package com.dentalmoovi.website.models.dtos;

import java.util.List;

public record ProductsDTO(
    long id,
    String nameProduct,
    double unitPrice,
    String description,
    String shortDescription,
    int stock,
    List<ImagesDTO> images,
    List<String> location,
    String hidden,
    String category
) {}
