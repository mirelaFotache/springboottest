package com.pentalog.bookstore.controllers;

import com.pentalog.bookstore.dto.*;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Book;
import com.pentalog.bookstore.services.BookService;
import com.pentalog.bookstore.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;

@RestController
@RequestMapping("/books")
public class BookController {

    @Resource
    private BookService bookService;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Resource
    private BookingService bookingService;
    @Autowired
    private BookingMapper bookingMapper;

    /**
     * Find all categories where a book is registered
     * @param bookId book id
     * @return categories where boo kis registered
     */
    @RequestMapping(value = "/{book_id}/categories/", method = RequestMethod.GET)
    public ResponseEntity<Collection<CategoryDTO>> getBookCategories(@PathVariable("book_id") Integer bookId) {
        return new ResponseEntity<>(bookService.findCategoriesByBookId(bookId), HttpStatus.OK);
    }

    /**
     * Find all bookings for given book
     * @param bookId book id
     * @return all bookings for given book
     */
    @RequestMapping(value = "/{book_id}/bookings/", method = RequestMethod.GET)
    public ResponseEntity<Collection<BookingDTO>> getBookingsByBook(@PathVariable("book_id") Integer bookId) {
        return new ResponseEntity<>(bookingMapper.toBookingDTOs(bookingService.findBookingsByBookId(bookId)), HttpStatus.OK);
    }

    /**
     * Find book by id
     * @param id id
     * @return book by id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<BookDTO> getBook(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(bookMapper.toBookDTO(bookService.findById(id)), HttpStatus.OK);
    }

    /**
     * Find books by title
     * @param title title
     * @return books by title
     */
    @RequestMapping(value = "/title", method = RequestMethod.GET)
    public ResponseEntity<Collection<BookDTO>> findByTitle(@RequestParam("searchBy") String title) {
        return new ResponseEntity<>(bookService.findByTitle(title), HttpStatus.OK);
    }

    /**
     * Find books by author
     * @param author author
     * @return books by author
     */
    @RequestMapping(value = "/author", method = RequestMethod.GET)
    public ResponseEntity<Collection<BookDTO>> findByAuthor(@RequestParam("searchBy") String author) {
        return new ResponseEntity<>(bookService.findByAuthor(author), HttpStatus.OK);
    }

    /**
     * Find all books
     * @return books
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Collection<BookDTO>> getAllBooks() {
        return new ResponseEntity<>(bookService.findAllBooks(), HttpStatus.OK);
    }

    /**
     * Insert book
     * @param bookDTO book
     * @return inserted book
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<BookDTO> insertBook(@RequestBody BookDTO bookDTO) {
        final Book book = bookMapper.toBook(bookDTO);
        book.setBookCategories(categoryMapper.toCategories(bookDTO.getBookCategories()));
        return new ResponseEntity<>(bookMapper.toBookDTO(bookService.insert(book)), HttpStatus.OK);
    }

    /**
     * Update book
     * @param bookDTO book
     * @return updated book
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<BookDTO> updateBook(@PathVariable("id") Integer id, @RequestBody BookDTO bookDTO) {
        final Book book = bookMapper.toBook(bookDTO);
        book.setBookCategories(categoryMapper.toCategories(bookDTO.getBookCategories()));
        final Book updatedBook = bookService.update(id, book);
        final BookDTO booksDTO = bookMapper.toBookDTO(updatedBook);

        return new ResponseEntity<>(booksDTO, HttpStatus.OK);
    }

    /**
     * Delete book
     * @param id id
     * @return status message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteBook(@PathVariable("id") Integer id) {
        final Long deleted = bookService.delete(id);
        if (deleted != null && deleted.intValue() == 1)
            return new ResponseEntity<>("Book successfully deleted!", HttpStatus.NO_CONTENT);
        else
            throw new BookstoreException("Book not found!");
    }
}
