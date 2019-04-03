package com.pentalog.bookstore.controllers;

import com.pentalog.bookstore.dto.*;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Booking;
import com.pentalog.bookstore.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Resource
    private BookingService bookingService;
    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * Get all bookings
     * @return bookings
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Collection<BookingDTO>> getAllBookings() {
        return new ResponseEntity<>(bookingMapper.toBookingDTOs(bookingService.findAll()), HttpStatus.OK);
    }

    /**
     * Persist booking
     * @param bookingDTO booking
     * @return persisted booking
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<BookingDTO> insertBooking(@RequestBody BookingDTO bookingDTO) {
        final Booking booking = bookingMapper.toBooking(bookingDTO);
        booking.setBookingBook(bookMapper.toBook(bookingDTO.getBookingBook()));
        booking.setBookingUser(userMapper.toUser(bookingDTO.getBookingUser()));

        return new ResponseEntity<>(bookingMapper.toBookingDTO(bookingService.insert(booking)), HttpStatus.OK);
    }

    /**
     *
     * @param id id
     * @param bookingDTO booking
     * @return updated booking
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable("id") Integer id, @RequestBody BookingDTO bookingDTO) {
        final Booking booking = bookingMapper.toBooking(bookingDTO);
        booking.setBookingBook(bookMapper.toBook(bookingDTO.getBookingBook()));
        booking.setBookingUser(userMapper.toUser(bookingDTO.getBookingUser()));

        final BookingDTO persistedBookingDTO = bookingMapper.toBookingDTO(bookingService.update(id, booking));
        return new ResponseEntity<>(persistedBookingDTO, HttpStatus.OK);
    }

    /**
     * Delete booking
     * @param id id
     * @return status message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteBooking(@PathVariable("id") Integer id) {
        final Long deleted = bookingService.delete(id);
        if (deleted != null && deleted.intValue() == 1)
            return new ResponseEntity<>("Booking successfully deleted!", HttpStatus.NO_CONTENT);
        else
            throw new BookstoreException("Booking not found!");
    }
}
