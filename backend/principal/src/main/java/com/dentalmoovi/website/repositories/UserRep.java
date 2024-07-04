package com.dentalmoovi.website.repositories;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dentalmoovi.website.models.entities.Users;

public interface UserRep extends CrudRepository<Users, Long>{
    Boolean existsByEmailIgnoreCase(@Param("email") String email);

    @Query("SELECT * FROM users WHERE email = :email")
    Optional<Users> findByEmail(@Param("email") String email);
}
