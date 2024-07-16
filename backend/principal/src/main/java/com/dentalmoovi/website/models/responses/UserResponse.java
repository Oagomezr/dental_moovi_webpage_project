package com.dentalmoovi.website.models.responses;

import java.util.List;

import com.dentalmoovi.website.models.dtos.UserDTO;

public record UserResponse(
    List<UserDTO> data
) {
}
