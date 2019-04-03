package com.pentalog.bookstore.services;

import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Book;
import com.pentalog.bookstore.persistence.entities.Booking;
import com.pentalog.bookstore.persistence.entities.User;
import com.pentalog.bookstore.persistence.repositories.BookingJpaRepository;
import com.pentalog.bookstore.persistence.repositories.BooksJpaRepository;
import com.pentalog.bookstore.persistence.repositories.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BookingService {


    @Autowired
    private BookingJpaRepository bookingJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private BooksJpaRepository bookJpaRepository;

    /**
     * Get all bookings
     *
     * @return bookings
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Collection<Booking> findAll() {
        return bookingJpaRepository.findAll();
    }

    /**
     * Find all bookings for given book
     *
     * @param bookId book id
     * @return all bookings for given book
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Collection<Booking> findBookingsByBookId(Integer bookId) {
        return bookingJpaRepository.findBookingsByBookId(bookId);
    }

    /**
     * persist booking
     *
     * @param booking booking
     * @return persisted booking
     */
    public Booking insert(Booking booking) {
        User user = booking.getBookingUser();
        Book book = booking.getBookingBook();
        if (user != null) {
            User persistedUser = userJpaRepository.findById(user.getId()).orElse(null);
            booking.setBookingUser(persistedUser);
        }
        if(book!=null) {
            Book persistedBook = bookJpaRepository.findById(book.getId()).orElse(null);
            booking.setBookingBook(persistedBook);
        }
        return bookingJpaRepository.save(booking);
    }

    /**
     * Update booking
     * @param id id
     * @param booking booking
     * @return updated booking
     */
    public Booking update(Integer id, Booking booking) {
        Booking savedBooking;
        Booking persistedBooking = bookingJpaRepository.findById(id).orElse(null);

        if(persistedBooking!=null && booking!=null) {
            persistedBooking.setStartDate(booking.getStartDate());
            persistedBooking.setEstimatedEndDate(booking.getEstimatedEndDate());

            if(booking.getBookingBook()!=null) {
                Book book = bookJpaRepository.getOne(booking.getBookingBook().getId());
                persistedBooking.setBookingBook(book);
            }
            if(booking.getBookingUser()!=null) {
                User user = userJpaRepository.findById(booking.getBookingUser().getId()).orElse(null);
                persistedBooking.setBookingUser(user);
            }

            savedBooking = bookingJpaRepository.save(persistedBooking);
        }else{
            throw new BookstoreException("Booking not found");
        }
        return savedBooking;
    }

    /**
     * Delete booking
     * @param id id
     * @return 0 if booking not removed and 1 if booking removed successfully
     */
    public Long delete(Integer id) {
        return bookingJpaRepository.removeById(id);

    }

}
