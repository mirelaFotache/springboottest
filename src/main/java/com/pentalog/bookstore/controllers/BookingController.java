package com.pentalog.bookstore.controllers;

import com.pentalog.bookstore.dto.BookingDTO;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private MessageSource messageSource;

    /**
     * Get all bookings
     *
     * @return bookings
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<BookingDTO>> getAllBookings() {
        return new ResponseEntity<>(bookingService.findAll(), HttpStatus.OK);
    }

    /**
     * Display all books that one user has not returned yet. Active bookings have realEndDate null as long as the reserved book has not been returned
     *
     * @param userId user id
     * @return all active bookings per user
     */
    @RequestMapping(value = "/activeBookingsPerUser", method = RequestMethod.GET)
    public ResponseEntity<Collection<BookingDTO>> findActiveBookingsByUser(@RequestParam("userId") int userId) {
        final Collection<BookingDTO> activeBookingsByUserId = bookingService.findActiveBookingsByUserId(userId);
        if(activeBookingsByUserId!=null && activeBookingsByUserId.size()>0) {
            return new ResponseEntity<>(activeBookingsByUserId, HttpStatus.OK);
        }else{
            throw new BookstoreException(messageSource.getMessage("error.no.booking.found", null, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * Persist booking
     *
     * @param bookingDTO booking
     * @return persisted booking
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BookingDTO> insertBooking(@RequestBody BookingDTO bookingDTO) {
        return new ResponseEntity<>(bookingService.insert(bookingDTO), HttpStatus.OK);
    }

    /**
     * @param id         id
     * @param bookingDTO booking
     * @return updated booking
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable("id") Integer id, @RequestBody BookingDTO bookingDTO) {
        return new ResponseEntity<>(bookingService.update(id, bookingDTO), HttpStatus.OK);
    }

    /**
     * Delete booking
     *
     * @param id id
     * @return status message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteBooking(@PathVariable("id") Integer id) {
        final Long deleted = bookingService.delete(id);
        if (deleted != null && deleted.intValue() == 1)
            return new ResponseEntity<>(messageSource.getMessage("message.booking.deleted", null, LocaleContextHolder.getLocale()), HttpStatus.NO_CONTENT);
        else
            throw new BookstoreException(messageSource.getMessage("error.no.booking.found", null, LocaleContextHolder.getLocale()));
    }
}
