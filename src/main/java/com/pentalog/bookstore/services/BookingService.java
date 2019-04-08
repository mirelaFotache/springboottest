package com.pentalog.bookstore.services;

import com.pentalog.bookstore.dto.BookDTO;
import com.pentalog.bookstore.dto.BookingDTO;
import com.pentalog.bookstore.dto.BookingsMapper;
import com.pentalog.bookstore.dto.UserDTO;
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
import java.util.stream.Collectors;
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BookingService {


    @Autowired
    private BookingJpaRepository bookingJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private BooksJpaRepository bookJpaRepository;
    @Autowired
    private BookingsMapper bookingsMapper;

    /**
     * Get all bookings
     *
     * @return bookings
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Collection<BookingDTO> findAll() {
        return bookingJpaRepository.findAll().stream().map(bookingsMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Find all bookings for given book
     *
     * @param bookId book id
     * @return all bookings for given book
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Collection<BookingDTO> findBookingsByBookId(Integer bookId) {
        return bookingJpaRepository.findBookingsByBookId(bookId).stream().map(bookingsMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * persist booking
     *
     * @param bookingDTO bookingDTO
     * @return persisted bookingDTO
     */
    public BookingDTO insert(BookingDTO bookingDTO) {
        UserDTO userDTO = bookingDTO.getBookingUser();
        BookDTO bookDTO = bookingDTO.getBookingBook();
        Booking booking = bookingsMapper.fromDTO(bookingDTO);
        if (userDTO != null) {
            User persistedUser = userJpaRepository.findById(userDTO.getId()).orElse(null);
            booking.setBookingUser(persistedUser);
        }
        if(bookDTO!=null) {
            Book persistedBook = bookJpaRepository.findById(bookDTO.getId()).orElse(null);
            booking.setBookingBook(persistedBook);
        }
        return bookingsMapper.toDTO(bookingJpaRepository.save(booking));
    }

    /**
     * Update booking
     * @param id id
     * @param bookingDTO bookingDTO
     * @return updated bookingDTO
     */
    public BookingDTO update(Integer id, BookingDTO bookingDTO) {
        Booking persistedBooking = bookingJpaRepository.findById(id).orElse(null);

        if(persistedBooking!=null && bookingDTO!=null) {
            persistedBooking.setStartDate(bookingDTO.getStartDate());
            persistedBooking.setEstimatedEndDate(bookingDTO.getEstimatedEndDate());

            if(bookingDTO.getBookingBook()!=null) {
                Book book = bookJpaRepository.getOne(bookingDTO.getBookingBook().getId());
                persistedBooking.setBookingBook(book);
            }
            if(bookingDTO.getBookingUser()!=null) {
                User user = userJpaRepository.findById(bookingDTO.getBookingUser().getId()).orElse(null);
                persistedBooking.setBookingUser(user);
            }

            return bookingsMapper.toDTO(bookingJpaRepository.save(persistedBooking));
        }else{
            throw new BookstoreException("Booking not found");
        }
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
