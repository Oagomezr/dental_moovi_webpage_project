package com.dentalmoovi.website.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dentalmoovi.website.models.entities.Users;

public interface UserRep extends CrudRepository<Users, Long>{
    Boolean existsByEmailIgnoreCase(@Param("email") String email);

    @Query("SELECT * FROM users WHERE email = :email")
    Optional<Users> findByEmail(@Param("email") String email);

    @Query( "SELECT * FROM users\n" + //
            "WHERE id NOT IN (\n" + //
            "    SELECT ur.id_user\n" + //
            "    FROM users_roles ur\n" + //
            "    JOIN roles r ON ur.id_role = r.id\n" + //
            "    WHERE r.role = 'ADMIN_ROLE'\n" + //
            ");")
    List<Users> findUsers();

    @Query( "SELECT \n" + //
            "   (CASE WHEN password IS NULL THEN false\n" + //
            "         WHEN password IS NOT NULL THEN true END)\n" + //
            "FROM users \n" + //
            "WHERE email = :email")
    Boolean wasRegister(@Param("email") String email);
}
