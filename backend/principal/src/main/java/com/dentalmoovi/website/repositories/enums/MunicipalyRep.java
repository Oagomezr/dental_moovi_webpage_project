package com.dentalmoovi.website.repositories.enums;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dentalmoovi.website.models.entities.enums.MunicipalyCity;

public interface MunicipalyRep extends CrudRepository<MunicipalyCity, Integer>{
    @Query( "SELECT * FROM municipaly_city " + 
            "WHERE UPPER(name) LIKE UPPER(CONCAT('%', :name, '%')) " + 
            "AND id_departament = :id_departament")
    List<MunicipalyCity> findMunicipalyByContaining(@Param("name") String municipaly, @Param("id_departament") int idDepartament);

}