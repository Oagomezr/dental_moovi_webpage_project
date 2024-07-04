package com.dentalmoovi.website.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dentalmoovi.website.models.entities.Addresses;

@Repository
public interface AddressesRep extends CrudRepository<Addresses, Long>{
    
}
