package com.pentalog.bookstore.persistence.repositories;

import com.pentalog.bookstore.persistence.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserJpaRepository extends CommonRepository<User, Integer> {

    @Query("SELECT u FROM User u where u.userName=:userName ")
    User findByUserName(@Param("userName") String userName);

    @Query("SELECT u FROM User u where u.userName=:userName and u.password=:password ")
    User findByUserNameAndPass(@Param("userName") String userName, @Param("password") String password);
}
