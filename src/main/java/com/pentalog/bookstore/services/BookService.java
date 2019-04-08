package com.pentalog.bookstore.services;

import com.pentalog.bookstore.dto.BookDTO;
import com.pentalog.bookstore.dto.BooksMapper;
import com.pentalog.bookstore.dto.CategoryDTO;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Book;
import com.pentalog.bookstore.persistence.entities.Category;
import com.pentalog.bookstore.persistence.repositories.BooksJpaRepository;
import com.pentalog.bookstore.persistence.repositories.CategoryJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BookService {

    @Autowired
    private BooksJpaRepository booksJpaRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;
    @Autowired
    private BooksMapper booksMapper;

    public static final String BOOK_NOT_FOUND = "Book not found!";
    private Logger logger = LoggerFactory.getLogger(BookService.class);

    /**
     * Find categories asociated with given book
     * @param bookId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public Collection<CategoryDTO> findCategoriesByBookId(Integer bookId) {
        return booksJpaRepository.findBookCategories(bookId).stream().map(booksMapper::toCategoryDto).collect(Collectors.toList());
    }
    /**
     * Find all books
     * @return books list
     */
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public Collection<BookDTO> findAllBooks() {
        return booksJpaRepository.findAllBooks().stream().map(booksMapper::toDto).collect(Collectors.toList());

    }

    /**
     * Find books by title
     * @param title title
     * @return books list
     */
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public Collection<BookDTO> findByTitle(String title){
        return booksJpaRepository.findByTitle(title.toLowerCase()).stream().map(booksMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Find book by id
     * @param id id
     * @return book by id
     */
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public Book findById(Integer id) {
        return booksJpaRepository.findById(id).orElse(null);
    }

    /**
     * Find books by author
     * @param author author
     * @return books list
     */
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public Collection<BookDTO> findByAuthor(String author){
        return booksJpaRepository.findByAuthor(author.toLowerCase()).stream().map(booksMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Save book
     * @param book book
     * @return saved book
     */
    public Book insert(Book book) {
        List<Category> categories = book.getBookCategories();
        int counter = 0;
        if (categories != null && categories.size() > 0) {
            for (Category category : categories) {
                Category persistedCategory = categoryJpaRepository.findById(category.getId()).orElse(null);
                if(persistedCategory!=null){
                    persistedCategory.setName(categories.get(counter).getName());
                }
                counter++;
            }
        }
        return booksJpaRepository.save(book);
    }

    /**
     * Update book
     * @param book book
     * @return updated book
     */
    public Book update(Integer id, Book book) {
        Book savedBook;
        Book persistedBook = booksJpaRepository.findById(id).orElse(null);

        if(persistedBook!=null && book!=null) {
            persistedBook.setTitle(book.getTitle());
            persistedBook.setAuthor(book.getAuthor());
            persistedBook.setBookNumber(book.getBookNumber());
            persistedBook.setBookImage(book.getBookImage());
            persistedBook.setPublishedDate(book.getPublishedDate());
            persistedBook.getBookCategories().clear();

            for(Category c : book.getBookCategories()){
                Category category = categoryJpaRepository.findById(c.getId()).orElse(null);
                category.setBooks(null);
                persistedBook.getBookCategories().add(category);
            }

            savedBook = booksJpaRepository.save(persistedBook);
        }else{
            throw new BookstoreException(BOOK_NOT_FOUND);
        }
        return savedBook;
    }

    /**
     * Delete book by id
     * @param id id
     * @return 0 when user not removed and 1 if user removed successfully
     */
    public Long delete(Integer id) {
        return booksJpaRepository.removeById(id);

    }
}
