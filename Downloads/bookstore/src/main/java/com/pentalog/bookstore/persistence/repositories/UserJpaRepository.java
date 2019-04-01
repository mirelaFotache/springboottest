package com.pentalog.bookstore.persistence.repositories;

import com.pentalog.bookstore.persistence.entities.User;
import com.pentalog.bookstore.persistence.entities.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;


public interface UserJpaRepository extends CommonRepository<User, Integer> {

    @Query("SELECT u FROM User u where lower(u.userName) like %:userName%")
    Collection<User> findByUserName(@Param("userName") String userName);

    @Query("SELECT u.userRoles FROM User u  where u.id=:id")
    Collection<Role> findUserRoles(@Param("id") Integer id);

    @Query("SELECT u FROM User u  LEFT JOIN FETCH u.userRoles as ur where u.id=:id")
    User findUserByIdAndUserRoles(@Param("id") Integer id);

}
