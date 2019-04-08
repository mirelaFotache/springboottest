package com.pentalog.bookstore.persistence.repositories;

import com.pentalog.bookstore.persistence.entities.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface RoleJpaRepository extends CommonRepository<Role, Integer> {

    @Query("SELECT r FROM Role r where lower(r.name) like %:name%")
    Collection<Role> findByName(@Param("name") String roleName);

    @Query("SELECT u.userRoles FROM User u  where u.id=:id")
    Collection<Role> findUserRoles(@Param("id") Integer id);
}
