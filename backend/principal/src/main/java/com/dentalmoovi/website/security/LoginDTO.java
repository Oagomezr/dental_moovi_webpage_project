package com.dentalmoovi.website.security;

public record LoginDTO(
    String userName,
    String password,
    String code
) {
}