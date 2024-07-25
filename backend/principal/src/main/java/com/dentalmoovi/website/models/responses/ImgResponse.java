package com.dentalmoovi.website.models.responses;

import java.util.List;

import com.dentalmoovi.website.models.dtos.ImagesDTO;

public record ImgResponse(
    List<ImagesDTO> data
) {
}
