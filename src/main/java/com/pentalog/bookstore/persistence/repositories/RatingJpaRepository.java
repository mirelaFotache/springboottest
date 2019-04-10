package com.pentalog.bookstore.persistence.repositories;

import com.pentalog.bookstore.persistence.entities.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface RatingJpaRepository extends CommonRepository<Rating, Integer> {

    @Query("SELECT r FROM Rating r  where r.ratingUser.id=:id")
    Collection<Rating> findUserRatings(@Param("id") Integer id);

    @Query("SELECT r FROM Rating r LEFT JOIN Book b on r.ratingBook.id=b.id  where r.ratingBook.id=:id")
    Collection<Rating> findBookRatings(@Param("id") Integer id);
}
