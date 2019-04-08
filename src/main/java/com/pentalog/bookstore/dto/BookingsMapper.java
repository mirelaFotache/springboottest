package com.pentalog.bookstore.dto;

import com.pentalog.bookstore.persistence.entities.Book;
import com.pentalog.bookstore.persistence.entities.Booking;
import com.pentalog.bookstore.persistence.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookingsMapper {

    @Autowired
    private BooksMapper booksMapper;
    @Autowired
    private UsersMapper userMapper;


    /**
     * Convert BookingDTO to Booking
     *
     * @param bookingDTO bookingDTO
     * @return booking
     */
    public Booking fromDTO(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        booking.setId(bookingDTO.getId());
        booking.setStartDate(bookingDTO.getStartDate());
        booking.setEstimatedEndDate(bookingDTO.getEstimatedEndDate());
        booking.setRealEndDate(bookingDTO.getRealEndDate());
        return booking;
    }

    /**
     * ConvertBooking to BookingDTO
     *
     * @param booking booking
     * @return bookingDTO
     */
    public BookingDTO toDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setStartDate(booking.getStartDate());
        bookingDTO.setEstimatedEndDate(booking.getEstimatedEndDate());
        bookingDTO.setRealEndDate(booking.getRealEndDate());
        Book book = booking.getBookingBook();
        if (book != null) {
            bookingDTO.setBookingBook(booksMapper.toDto(book));
        }
        User user = booking.getBookingUser();
        if (user != null) {
            bookingDTO.setBookingUser(userMapper.toDTO(Optional.ofNullable(user)).get());
        }
        return bookingDTO;
    }
}
