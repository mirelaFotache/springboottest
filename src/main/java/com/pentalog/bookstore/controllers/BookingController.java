package com.pentalog.bookstore.controllers;

import com.pentalog.bookstore.dto.BookingDTO;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.services.BookingService;
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

    /**
     * Find bookings by title, author and availability
     *
     * @param title        title
     * @param author       author
     * @param availability availability
     * @return all found bookings
     */
    @RequestMapping(value = "/bookingPreferences", method = RequestMethod.GET)
    public ResponseEntity<Collection<BookingDTO>> getBookingsByTitleAuthorAvailability(@RequestParam("title") String title,
                                                                                       @RequestParam("author") String author,
                                                                                       @RequestParam("availability") boolean availability) {
        final Collection<BookingDTO> bookings = bookingService.findBookingsByTitleAuthorAvailability(title, author, availability);
        if (bookings != null && bookings.size() > 0)
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        else {
            throw new BookstoreException("No booking found!");
        }
    }

    /**
     * Get all bookings
     *
     * @return bookings
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Collection<BookingDTO>> getAllBookings() {
        return new ResponseEntity<>(bookingService.findAll(), HttpStatus.OK);
    }

    /**
     * Persist booking
     *
     * @param bookingDTO booking
     * @return persisted booking
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
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
            return new ResponseEntity<>("Booking successfully deleted!", HttpStatus.NO_CONTENT);
        else
            throw new BookstoreException("Booking not found!");
    }
}
