package com.dentalmoovi.website.models.responses;

import java.util.List;

import com.dentalmoovi.website.models.dtos.Enum1DTO;

public record EnumResponse1(
    List<Enum1DTO> data
    ) {
}

