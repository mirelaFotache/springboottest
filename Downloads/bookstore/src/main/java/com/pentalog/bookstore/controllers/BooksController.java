package com.pentalog.bookstore.controllers;

import com.pentalog.bookstore.dto.*;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Book;
import com.pentalog.bookstore.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;

@RestController
@RequestMapping("/books")
public class BooksController {

    @Resource
    private BooksService booksService;
    @Autowired
    private BooksMapper booksMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CartMapper cartMapper;

    @RequestMapping(value = "/{book_id}/categories/", method = RequestMethod.GET)
    public ResponseEntity<Collection<CategoryDTO>> getBookCategories(@PathVariable("book_id") Integer userId) {
        return new ResponseEntity<>(categoryMapper.toCategoryDTOs(booksService.findCategoriesByBookId(userId)), HttpStatus.OK);
    }

    /**
     * Find book by id
     * @param id id
     * @return book by id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<BooksDTO> getBook(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(booksMapper.toBooksDTO(booksService.findById(id)), HttpStatus.OK);
    }

    /**
     * Find books by title
     * @param title title
     * @return books by title
     */
    @RequestMapping(value = "/title", method = RequestMethod.GET)
    public ResponseEntity<Collection<BooksDTO>> findByTitle(@RequestParam("searchBy") String title) {
        return new ResponseEntity<>(booksMapper.toBooksDTOs(booksService.findByTitle(title)), HttpStatus.OK);
    }

    /**
     * Find books by author
     * @param author author
     * @return books by author
     */
    @RequestMapping(value = "/author", method = RequestMethod.GET)
    public ResponseEntity<Collection<BooksDTO>> findByAuthor(@RequestParam("searchBy") String author) {
        return new ResponseEntity<>(booksMapper.toBooksDTOs(booksService.findByAuthor(author)), HttpStatus.OK);
    }

    /**
     * Find all books
     * @return books
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Collection<BooksDTO>> getAllBooks() {
        return new ResponseEntity<>(booksMapper.toBooksDTOs(booksService.findAll()), HttpStatus.OK);
    }

    /**
     * Insert book
     * @param bookDTO book
     * @return inserted book
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<BooksDTO> insertBook(@RequestBody BooksDTO bookDTO) {
        final Book book = booksMapper.toBooks(bookDTO);
        book.setBookCategories(categoryMapper.toCategories(bookDTO.getBookCategories()));
        book.setCarts(cartMapper.toCarts(bookDTO.getCarts()));
        return new ResponseEntity<>(booksMapper.toBooksDTO(booksService.insert(book)), HttpStatus.OK);
    }

    /**
     * Update book
     * @param bookDTO book
     * @return updated book
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<BooksDTO> updateBook(@PathVariable("id") Integer id, @RequestBody BooksDTO bookDTO) {
        final Book book = booksMapper.toBooks(bookDTO);
        book.setBookCategories(categoryMapper.toCategories(bookDTO.getBookCategories()));
        book.setCarts(cartMapper.toCarts(bookDTO.getCarts()));
        return new ResponseEntity<>(booksMapper.toBooksDTO(booksService.update(id, book)), HttpStatus.OK);
    }

    /**
     * Delete book
     * @param id id
     * @return status message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteBook(@PathVariable("id") Integer id) {
        final Long deleted = booksService.delete(id);
        if (deleted != null && deleted.intValue() == 1)
            return new ResponseEntity<>("Book successfully deleted!", HttpStatus.NO_CONTENT);
        else
            throw new BookstoreException("Book not found!");
    }
}
