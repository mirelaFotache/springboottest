package com.pentalog.bookstore.controllers;

import com.pentalog.bookstore.dto.RatingDTO;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Resource
    private RatingService ratingService;
    @Autowired
    private MessageSource messageSource;

    /**
     * Get all ratings associated with given user
     *
     * @param userId user id
     * @return ratings for given user
     */
    @RequestMapping(value = "/{user_id}/ratingsPerUser", method = RequestMethod.GET)
    public ResponseEntity<Collection<RatingDTO>> getRatingsPerUser(@PathVariable("user_id") Integer userId) {
        return new ResponseEntity<>(ratingService.findRatingsPerUser(userId), HttpStatus.OK);
    }

    /**
     * Get all ratings associated with given book
     *
     * @param bookId book id
     * @return ratings for given book
     */
    @RequestMapping(value = "/{book_id}/ratingsPerBook", method = RequestMethod.GET)
    public ResponseEntity<Collection<RatingDTO>> getRatingsPerBook(@PathVariable("book_id") Integer bookId) {
        return new ResponseEntity<>(ratingService.findRatingsPerBook(bookId), HttpStatus.OK);
    }

    /**
     * Get all ratings
     *
     * @return all ratings
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<RatingDTO>> getAllRatings() {
        return new ResponseEntity<>(ratingService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/preferences", method = RequestMethod.GET)
    public ResponseEntity<Collection<RatingDTO>> getRatingsPerUserAndBook(@RequestParam("user_id") Integer userId, @RequestParam("book_id") Integer bookId) {
        return new ResponseEntity<>(ratingService.findRatingsPerUserAndBook(userId, bookId), HttpStatus.OK);
    }

    /**
     * Persist rating
     *
     * @param ratingDTO ratingDTO
     * @return persisted ratingDTO
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RatingDTO> insertRating(@RequestBody RatingDTO ratingDTO) {
        return new ResponseEntity<>(ratingService.insert(ratingDTO), HttpStatus.OK);
    }

    /**
     * Update rating
     *
     * @param id        id
     * @param ratingDTO ratingDTO
     * @return updated ratingDTO
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<RatingDTO> updateRating(@PathVariable("id") Integer id, @RequestBody RatingDTO ratingDTO) {
        return new ResponseEntity<>(ratingService.update(id, ratingDTO), HttpStatus.OK);
    }

    /**
     * Delete rating by id
     *
     * @param id id
     * @return status for deleting operation
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteRating(@PathVariable("id") Integer id) {
        final Long deleted = ratingService.delete(id);
        if (deleted != null && deleted.intValue() == 1)
            return new ResponseEntity<>(messageSource.getMessage("message.rating.deleted", null, LocaleContextHolder.getLocale()), HttpStatus.NO_CONTENT);
        else
            throw new BookstoreException(messageSource.getMessage("error.no.booking.found", null, LocaleContextHolder.getLocale()));
    }
}
