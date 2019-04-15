package com.pentalog.bookstore.utils;

import com.pentalog.bookstore.dto.BookDTO;
import com.pentalog.bookstore.dto.BookingDTO;
import com.pentalog.bookstore.dto.UserDTO;
import com.pentalog.bookstore.persistence.entities.Booking;
import com.pentalog.bookstore.persistence.entities.Book;
import com.pentalog.bookstore.persistence.entities.User;
import java.util.Optional;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class BookingSupplier {

    /**
     * Supply booking for insert
     *
     * @return booking
     */
    public static Booking supplyBookingForInsertNotAllowed() {
        Booking booking = new Booking();
        booking.setId(1);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDateTime localStartDate = LocalDateTime.of(2019, 03, 01, 00, 00, 00);
        java.util.Date startDate = java.util.Date.from(localStartDate.atZone(defaultZoneId).toInstant());
        java.util.Date estimatedEndDate = java.util.Date.from(localStartDate.atZone(defaultZoneId).plusDays(24L).toInstant());
        booking.setStartDate(startDate);
        booking.setEstimatedEndDate(estimatedEndDate);
        Book book = new Book();
        book.setId(3);
        booking.setBookingBook(book);
        User user = new User();
        user.setId(1);
        booking.setBookingUser(user);
        return booking;
    }

    /**
     * Supply bookingDTO for insert operation
     *
     * @return bookingDTO
     */
    public static BookingDTO supplyBookingDTOForInsertNotAllowed() {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(1);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDateTime localStartDate = LocalDateTime.of(2019, 03, 01, 00, 00, 00);
        java.util.Date startDate = java.util.Date.from(localStartDate.atZone(defaultZoneId).toInstant());
        java.util.Date estimatedEndDate = java.util.Date.from(localStartDate.atZone(defaultZoneId).plusDays(24L).toInstant());
        bookingDTO.setStartDate(startDate);
        bookingDTO.setEstimatedEndDate(estimatedEndDate);
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(3);
        bookingDTO.setBookingBook(bookDTO);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        bookingDTO.setBookingUser(userDTO);
        return bookingDTO;
    }

    /**
     * Set bookingDTO parameters
     * @return bookingDTO
     */
    public static BookingDTO supplyBookingDTOForInsertNotAllowedUserIdZero() {
        BookingDTO bookingDTO = new BookingDTO();
        setBookingDTOParameters(bookingDTO);
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(3);
        bookingDTO.setBookingBook(bookDTO);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(0);
        bookingDTO.setBookingUser(userDTO);
        return bookingDTO;
    }
    public static BookingDTO supplyBookingDTOForInsertNotAllowedBookIdZero() {
        BookingDTO bookingDTO = new BookingDTO();
        setBookingDTOParameters(bookingDTO);
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(0);
        bookingDTO.setBookingBook(bookDTO);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        bookingDTO.setBookingUser(userDTO);
        return bookingDTO;
    }

    private static void setBookingDTOParameters(BookingDTO bookingDTO) {
        bookingDTO.setId(1);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDateTime localStartDate = LocalDateTime.of(2019, 03, 01, 00, 00, 00);
        java.util.Date startDate = java.util.Date.from(localStartDate.atZone(defaultZoneId).toInstant());
        java.util.Date estimatedEndDate = java.util.Date.from(localStartDate.atZone(defaultZoneId).plusDays(24L).toInstant());
        bookingDTO.setStartDate(startDate);
        bookingDTO.setEstimatedEndDate(estimatedEndDate);
        bookingDTO.setRealEndDate(estimatedEndDate);
    }
    /**
     * Supply booking for insert
     *
     * @return booking
     */
    public static Booking supplyBookingForInsertNoAvailableStock() {
        Booking booking = new Booking();
        setBookingParameters(booking);
        Book book = new Book();
        book.setId(3);
        booking.setBookingBook(book);
        User user = new User();
        user.setId(1);
        booking.setBookingUser(user);
        return booking;
    }

    /**
     * Supply booking for insert
     *
     * @return booking
     */
    public static Booking supplyBookingForInsertAllowed() {
        Booking booking = new Booking();
        setBookingParameters(booking);
        Book book = new Book();
        book.setId(3);
        booking.setBookingBook(book);
        User user = new User();
        user.setId(1);
        booking.setBookingUser(user);
        return booking;
    }

    public static Optional<Book> supplyBookForInsert(){
        Book book = new Book();
        book.setId(3);
        return Optional.of(book);
    }

    /**
     * Supply bookingDTO for insert operation
     *
     * @return bookingDTO
     */
    public static BookingDTO supplyBookingDTOForInsertAllowed() {
        BookingDTO bookingDTO = new BookingDTO();
        setBookingDTOParameters(bookingDTO);
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(3);
        bookingDTO.setBookingBook(bookDTO);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        bookingDTO.setBookingUser(userDTO);
        return bookingDTO;
    }
    public static BookingDTO supplyBookingDTOForInsertNoAvailableStock() {
        BookingDTO bookingDTO = new BookingDTO();
        setBookingDTOParameters(bookingDTO);
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(3);
        bookingDTO.setBookingBook(bookDTO);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        bookingDTO.setBookingUser(userDTO);
        return bookingDTO;
    }

    public static Booking supplyBookingForInsertNotAllowedUserIdZero() {
        Booking booking = new Booking();
        setBookingParameters(booking);
        Book book = new Book();
        book.setId(3);
        booking.setBookingBook(book);
        User user = new User();
        user.setId(0);
        booking.setBookingUser(user);
        return booking;
    }


    public static Booking supplyBookingForInsertNotAllowedBookIdZero() {
        Booking booking = new Booking();
        setBookingParameters(booking);
        Book book = new Book();
        book.setId(0);
        booking.setBookingBook(book);
        User user = new User();
        user.setId(1);
        booking.setBookingUser(user);
        return booking;
    }

    /**
     * Set booking parameters
     * @param booking booking
     */
    private static void setBookingParameters(Booking booking) {
        booking.setId(1);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDateTime localStartDate = LocalDateTime.of(2019, 03, 01, 00, 00, 00);
        java.util.Date startDate = java.util.Date.from(localStartDate.atZone(defaultZoneId).toInstant());
        java.util.Date estimatedEndDate = java.util.Date.from(localStartDate.atZone(defaultZoneId).plusDays(24L).toInstant());
        booking.setStartDate(startDate);
        booking.setEstimatedEndDate(estimatedEndDate);
        booking.setRealEndDate(estimatedEndDate);
    }
}
