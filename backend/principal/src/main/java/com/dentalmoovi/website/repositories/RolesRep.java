package com.dentalmoovi.website.repositories;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.entities.enums.RolesList;

public interface RolesRep extends CrudRepository<Roles, Long>{
    @Query("SELECT * FROM roles WHERE role = :name_role")
    public Optional<Roles> findByRole(@Param("name_role") RolesList nameRole);
}