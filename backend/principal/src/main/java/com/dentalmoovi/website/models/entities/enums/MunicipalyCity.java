package com.dentalmoovi.website.models.entities.enums;

import org.springframework.data.annotation.Id;

public record MunicipalyCity(
    @Id Integer id,
    String name,
    Integer id_departament
) {} 
