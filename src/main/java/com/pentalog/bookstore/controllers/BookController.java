package com.pentalog.bookstore.controllers;

import com.pentalog.bookstore.dto.BookDTO;
import com.pentalog.bookstore.dto.BookingDTO;
import com.pentalog.bookstore.dto.CategoryDTO;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.services.BookService;
import com.pentalog.bookstore.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private MessageSource messageSource;

    /**
     * Find bookings by title, author and availability
     *
     * @param title        title
     * @param author       author
     * @param availability availability
     * @return all found bookings
     */
    @RequestMapping(value = "/bookingPreferences", method = RequestMethod.GET)
    public ResponseEntity<Collection<BookDTO>> getBooksByTitleAuthorAvailability(@RequestParam("title") String title,
                                                                                 @RequestParam("author") String author,
                                                                                 @RequestParam("availability") String availability) {
        final Collection<BookDTO> availableBooksByTitleAndAuthor = bookService.findBooksByTitleAuthorAndAvailability(title, author, availability);
        if (availableBooksByTitleAndAuthor != null && availableBooksByTitleAndAuthor.size() > 0)
            return new ResponseEntity<>(availableBooksByTitleAndAuthor, HttpStatus.OK);
        else {
            throw new BookstoreException(messageSource.getMessage("error.no.book.found",null, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * Find all categories where a book is registered
     *
     * @param bookId book id
     * @return categories where boo kis registered
     */
    @RequestMapping(value = "/{book_id}/categories/", method = RequestMethod.GET)
    public ResponseEntity<Collection<CategoryDTO>> getBookCategories(@PathVariable("book_id") Integer bookId) {
        return new ResponseEntity<>(bookService.findCategoriesByBookId(bookId), HttpStatus.OK);
    }

    /**
     * Find all bookings for given book
     *
     * @param bookId book id
     * @return all bookings for given book
     */
    @RequestMapping(value = "/{book_id}/bookings/", method = RequestMethod.GET)
    public ResponseEntity<Collection<BookingDTO>> getBookingsByBook(@PathVariable("book_id") Integer bookId) {
        return new ResponseEntity<>(bookingService.findBookingsByBookId(bookId), HttpStatus.OK);
    }

    /**
     * Find book by id
     *
     * @param id id
     * @return book by id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<BookDTO> getBook(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(bookService.findById(id), HttpStatus.OK);
    }

    /**
     * Find books by title
     *
     * @param title title
     * @return books by title
     */
    @RequestMapping(value = "/title", method = RequestMethod.GET)
    public ResponseEntity<Collection<BookDTO>> findByTitle(@RequestParam("searchBy") String title) {
        return new ResponseEntity<>(bookService.findByTitle(title), HttpStatus.OK);
    }

    /**
     * Find books by author
     *
     * @param author author
     * @return books by author
     */
    @RequestMapping(value = "/author", method = RequestMethod.GET)
    public ResponseEntity<Collection<BookDTO>> findByAuthor(@RequestParam("searchBy") String author) {
        return new ResponseEntity<>(bookService.findByAuthor(author), HttpStatus.OK);
    }

    /**
     * Find all books
     *
     * @return books
     */
    @RequestMapping(value = "/booksAvailability", method = RequestMethod.GET)
    public ResponseEntity<Collection<BookDTO>> getBooksAvailability() {
        return new ResponseEntity<>(bookService.findBooksAvailability(), HttpStatus.OK);
    }

    /**
     * Insert book
     *
     * @param bookDTO book
     * @return inserted book
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<BookDTO> insertBook(@RequestBody BookDTO bookDTO) {
        final BookDTO insertedBook = bookService.insert(bookDTO);
        return new ResponseEntity<>(insertedBook, HttpStatus.OK);

    }

    /**
     * Update book
     *
     * @param bookDTO book
     * @return updated book
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<BookDTO> updateBook(@PathVariable("id") Integer id, @RequestBody BookDTO bookDTO) {
        return new ResponseEntity<>(bookService.update(id, bookDTO), HttpStatus.OK);
    }

    /**
     * Delete book
     *
     * @param id id
     * @return status message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteBook(@PathVariable("id") Integer id) {
        final Long deleted = bookService.delete(id);
        if (deleted != null && deleted.intValue() == 1)
            return new ResponseEntity<>(messageSource.getMessage("message.book.deleted",null, LocaleContextHolder.getLocale()), HttpStatus.NO_CONTENT);
        else
            throw new BookstoreException(messageSource.getMessage("error.no.book.found",null, LocaleContextHolder.getLocale()));
    }
}
