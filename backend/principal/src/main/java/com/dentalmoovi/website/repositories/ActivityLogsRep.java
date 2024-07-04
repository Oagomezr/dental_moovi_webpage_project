package com.dentalmoovi.website.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dentalmoovi.website.models.entities.ActivityLogs;

@Repository
public interface ActivityLogsRep extends CrudRepository<ActivityLogs, Long>{
    
}
