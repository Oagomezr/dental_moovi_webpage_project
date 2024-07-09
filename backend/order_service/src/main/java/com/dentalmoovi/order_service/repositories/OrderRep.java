package com.dentalmoovi.order_service.repositories;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dentalmoovi.order_service.models.Order;

@Repository
public interface OrderRep extends CrudRepository<Order, Long>{
    @Modifying
    @Transactional
    @Query("UPDATE orders o SET o.order_file = :orderFile WHERE o.id = :id")
    int updateOrderFileById(@Param("orderFile") byte[] orderFile, @Param("id") Long id);
}