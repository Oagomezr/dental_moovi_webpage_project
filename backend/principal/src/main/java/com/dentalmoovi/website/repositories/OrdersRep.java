package com.dentalmoovi.website.repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dentalmoovi.website.models.dtos.OrderDTO;
import com.dentalmoovi.website.models.entities.Orders;

public interface OrdersRep extends CrudRepository<Orders, Long>{

    final String P1 = 
    "SELECT \n" + //
        "o.id as id_order,\n" + //
        "u.first_name,\n" + //
        "u.last_name,\n" + //
        "mc.name as municipaly,\n" + //
        "d.name as departament,\n" + //
        "a.address,\n" + //
        "DATE_FORMAT(o.date, '%H:%i %d/%m/%Y') as date\n" + //
    "FROM orders o\n" + //
    "INNER JOIN users u on u.id = o.id_user\n" + //
    "INNER JOIN addresses a on a.id = id_address\n" + //
    "INNER JOIN municipaly_city mc on mc.id = a.id_municipaly_city\n" + //
    "INNER JOIN departaments d on d.id = mc.id_departament\n" + //
    "WHERE o.status = :status\n"; //

    final String P2 = "GROUP BY o.id\n" + //
    "ORDER BY o.date";

    final String QUERY1 = P1 + P2;

    final String QUERY2 = P1 + "AND u.id = :id\n" + P2;

    @Query(QUERY1 +" asc;")
    List<OrderDTO> findAllOrdersAsc(@Param("status") String status);

    @Query(QUERY1+" desc;")
    List<OrderDTO> findAllOrdersDesc(@Param("status") String status);

    @Query(QUERY2 +" asc;")
    List<OrderDTO> findOrdersAsc(@Param("status") String status, @Param("id") Long id);

    @Query(QUERY2+" desc;")
    List<OrderDTO> findOrdersDesc(@Param("status") String status, @Param("id") Long id);

    @Query("SELECT * FROM dental_moovi.orders WHERE id = :id;")
    Orders findPdfBytes(@Param("id") Long id);
    
}
