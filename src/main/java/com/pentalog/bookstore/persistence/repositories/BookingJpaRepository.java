package com.pentalog.bookstore.persistence.repositories;

import com.pentalog.bookstore.persistence.entities.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface BookingJpaRepository extends CommonRepository<Booking, Integer> {

@Query("SELECT b FROM Booking b where b.bookingBook.id=:bookId")
Collection<Booking> findBookingsByBookId(@Param("bookId") int bookId);
}
