package com.dentalmoovi.website.models.responses;

import java.util.List;

import com.dentalmoovi.website.models.dtos.CategoriesDTO;


public record CategoriesResponse(
    List<CategoriesDTO> data
) {}

