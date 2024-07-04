package com.dentalmoovi.website.repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dentalmoovi.website.models.entities.Images;

public interface ImgRep extends CrudRepository<Images, Long>{
    @Query("SELECT * FROM images WHERE id_product = :idProduct")
    List<Images> findByIdProduct(@Param("idProduct") Long idProduct);
}
