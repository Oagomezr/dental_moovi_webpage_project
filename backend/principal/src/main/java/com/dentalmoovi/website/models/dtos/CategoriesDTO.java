package com.dentalmoovi.website.models.dtos;

import java.util.List;

public record CategoriesDTO(
    List<String> categoryAndParents,
    List<CategoriesDTO> childrenCategories
){}
