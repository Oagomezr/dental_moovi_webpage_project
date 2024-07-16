package com.dentalmoovi.website.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dentalmoovi.website.models.entities.Enterprises;

@Repository
public interface EnterprisesRep extends CrudRepository<Enterprises, Long>{
    @Query("SELECT * FROM enterprises WHERE UPPER(name) LIKE UPPER(CONCAT('%', :name, '%')) LIMIT 7")
    List<Enterprises> findEnterprises(@Param("name") String name);

    @Query("SELECT * FROM enterprises WHERE name = :name")
    Optional<Enterprises> findByName(@Param("name") String name);

    Boolean existsByNameIgnoreCase(@Param("name") String name);
}
