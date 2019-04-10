package com.pentalog.bookstore.persistence.repositories;

import com.pentalog.bookstore.persistence.entities.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface BookingJpaRepository extends CommonRepository<Booking, Integer> {

    @Query("SELECT b FROM Booking b where b.bookingBook.id=:bookId")
    Collection<Booking> findBookingsByBookId(@Param("bookId") int bookId);

    @Query("SELECT count(b) FROM Booking b where b.bookingUser.id=:userId and b.realEndDate is null")
    Integer findBookingsByUserId(@Param("userId") int userId);

    @Query("SELECT b FROM Booking b LEFT JOIN Book book on b.bookingBook.id = book.id where (book.title=:title or book.author=:author) and b.realEndDate is not null")
    Collection<Booking>  findBookingsByTitleAuthorAvailabilityTrue(@Param("title") String title, @Param("author") String author);

    @Query("SELECT b FROM Booking b LEFT JOIN Book book on b.bookingBook.id = book.id where (book.title=:title or book.author=:author) and b.realEndDate is null")
    Collection<Booking>  findBookingsByTitleAuthorAvailabilityFalse(@Param("title") String title, @Param("author") String author);
}
