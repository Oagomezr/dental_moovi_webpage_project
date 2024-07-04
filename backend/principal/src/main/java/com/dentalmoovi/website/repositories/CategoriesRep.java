package com.dentalmoovi.website.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dentalmoovi.website.models.entities.Categories;

@Repository
public interface CategoriesRep extends CrudRepository<Categories, Long>{
    @Query("SELECT * FROM categories WHERE id_parent_category IS NULL ORDER BY name")
    List<Categories> findParentCategories();

    @Query("SELECT * FROM categories WHERE id_parent_category = :parentCategoryId ORDER BY name")
    List<Categories> findByParentCategory(@Param("parentCategoryId") Long parentCategoryId);

    @Query("SELECT * FROM categories WHERE name = :name")
    Optional<Categories> findByName(@Param("name") String name);

    @Query("SELECT * FROM categories WHERE name LIKE UPPER(CONCAT('%', :name, '%')) LIMIT 7")
    List<Categories> findByContaining(@Param("name") String name);
}
