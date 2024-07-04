package com.dentalmoovi.website.repositories.enums;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dentalmoovi.website.models.entities.enums.Departaments;

public interface DepartamentsRep extends CrudRepository<Departaments, Integer>{
    
    @Query("SELECT * FROM departaments WHERE UPPER(name) LIKE UPPER(CONCAT('%', :name, '%')) LIMIT 7")
    List<Departaments> findDepartamentByContaining(@Param("name") String name);
}
