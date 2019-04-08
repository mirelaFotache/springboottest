package com.pentalog.bookstore.persistence.repositories;

import com.pentalog.bookstore.persistence.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;


public interface UserJpaRepository extends CommonRepository<User, Integer> {

    @Query("SELECT u FROM User u where lower(u.userName) like %:userName% and :userName!=''")
    Collection<User> findByUserName(@Param("userName") String userName);

}
