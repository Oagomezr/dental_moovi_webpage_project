package com.dentalmoovi.website.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.dentalmoovi.website.models.entities.Products;

public interface ProductsRep extends CrudRepository<Products,Long>{
    @Query("SELECT p.* FROM products p JOIN categories c ON p.id_category = c.id WHERE c.name = :categoryName ORDER BY p.name")
    List<Products> findByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT * FROM products WHERE name = :nameProduct")
    Optional<Products> findByName(@Param("nameProduct") String nameProduct);

    @Query("SELECT * FROM products WHERE UPPER(name) LIKE UPPER(CONCAT('%', :name, '%')) LIMIT :limit OFFSET :offset")
    List<Products> findByNameContaining(@Param("name") String name, @Param("limit") int limit, @Param("offset") int offset);

    @Query("SELECT count(*) FROM products WHERE UPPER(name) LIKE UPPER(CONCAT('%', :name, '%'))")
    int countProductsByContaining(@Param("name") String name);

    boolean existsByName(@Param("name") String name);

    @Query("SELECT EXISTS(SELECT 1 FROM orders_products WHERE id_product = :productId)")
    boolean existsRelacionsProduct(@Param("productId") Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM orders_products WHERE id_product = :productId")
    void deleteRelacionsProduct(@Param("productId") Long id);

}
