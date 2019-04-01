package com.pentalog.bookstore.persistence.repositories;

import com.pentalog.bookstore.persistence.entities.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface CategoryJpaRepository extends CommonRepository<Category, Integer> {

    @Query("SELECT c FROM Category c where lower(c.name) like %:name%")
    Collection<Category> findByName(@Param("name") String categoryName);
}
