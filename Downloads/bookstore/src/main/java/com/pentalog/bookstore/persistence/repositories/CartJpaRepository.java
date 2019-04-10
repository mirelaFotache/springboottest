package com.pentalog.bookstore.persistence.repositories;

import com.pentalog.bookstore.persistence.entities.Cart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface CartJpaRepository extends CommonRepository<Cart, Integer> {

    @Query("SELECT c FROM Cart c where lower(c.title) like %:title%")
    Collection<Cart> findByTitle(@Param("title") String title);
}
