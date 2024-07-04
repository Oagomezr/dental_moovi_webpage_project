package com.dentalmoovi.website.models.responses;

import java.util.List;

import com.dentalmoovi.website.models.dtos.AddressesDTO;

public record AddressesResponse(
    List<AddressesDTO> data
) {}
