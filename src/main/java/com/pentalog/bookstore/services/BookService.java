package com.pentalog.bookstore.services;

import com.pentalog.bookstore.dto.BookDTO;
import com.pentalog.bookstore.dto.BooksMapper;
import com.pentalog.bookstore.dto.CategoryDTO;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Book;
import com.pentalog.bookstore.persistence.entities.Booking;
import com.pentalog.bookstore.persistence.entities.Category;
import com.pentalog.bookstore.persistence.repositories.BooksJpaRepository;
import com.pentalog.bookstore.persistence.repositories.CategoryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class BookService {

    @Autowired
    private BooksJpaRepository booksJpaRepository;
    @Autowired
    private CategoryJpaRepository categoryJpaRepository;
    @Autowired
    private BooksMapper booksMapper;
    @Autowired
    private EntityManager em;
    @Autowired
    private MessageSource messageSource;

    /**
     * Find all books
     * @return books
     */
    public Collection<BookDTO> findAll() {
        return booksJpaRepository.findAll().stream().map(booksMapper::toDto).collect(Collectors.toList());
    }
    /**
     * Find books by title, author and availability
     *
     * @param title        title
     * @param author       author
     * @param availability availability
     * @return books found based on criteria
     */
    public Collection<BookDTO> findBooksByTitleAuthorAndAvailability(String title, String author, String availability) {
        List<Book> books = new ArrayList<>();

        final Collection<Book> booksByTitleOrAuthor = booksJpaRepository.findBooksByTitleOrAuthor(title, author);
        for (Book book : booksByTitleOrAuthor) {
            //If user requested only for the available books then check if stock per book is greater than the number of reservations
            if ("0".equals(availability)) {
                if (book.getStock() >= book.getStockAvailableBook() && book.getStockAvailableBook() != 0) {
                    books.add(book);
                }
            }//If user requested only for the unavailable books then check if stock per book is equal with the number of reservations
            else if ("1".equals(availability)) {
                if (book.getStockAvailableBook() == 0) {
                    books.add(book);
                }
            }
        }
        return books.stream().map(booksMapper::toAvailableBookDto).collect(Collectors.toList());
    }

    /**
     * Find categories asociated with given book
     *
     * @param bookId bookId
     * @return Categories
     */
    public Collection<CategoryDTO> findCategoriesByBookId(Integer bookId) {
        return booksJpaRepository.findBookCategories(bookId).stream().map(booksMapper::toCategoryDto).collect(Collectors.toList());
    }

    /**
     * Find all books. For each book display the most recent booking if any was found and the user who reserved the book
     *
     * @return books list
     */
    public Collection<BookDTO> findBooksAvailability() {
        final List<Book> allBooks = booksJpaRepository.findAllBooks();
        for (Book book : allBooks) {
            String hql = "select b from Booking b where b.bookingBook.id=:id order by b.startDate desc";
            TypedQuery<Booking> query = em.createQuery(hql, Booking.class);
            query.setParameter("id", book.getId());
            query.setMaxResults(1);
            Booking booking = query.getResultList().stream().findFirst().orElse(null);
            book.setBooking(booking);
        }
        return allBooks.stream().map(booksMapper::toAvailableBookDto).collect(Collectors.toList());
    }

    /**
     * Find books by title
     *
     * @param title title
     * @return books list
     */
    public Collection<BookDTO> findByTitle(String title) {
        return booksJpaRepository.findByTitle(title.toLowerCase()).stream().map(booksMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Find book by id
     *
     * @param id id
     * @return book by id
     */
    public BookDTO findById(Integer id) {
        return booksMapper.toDto(booksJpaRepository.findById(id).orElse(null));
    }

    /**
     * Find books by author
     *
     * @param author author
     * @return books list
     */
    public Collection<BookDTO> findByAuthor(String author) {
        return booksJpaRepository.findByAuthor(author.toLowerCase()).stream().map(booksMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Save book
     *
     * @param bookDTO bookDTO
     * @return bookDTO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public BookDTO insert(BookDTO bookDTO) {
        Book book = booksMapper.fromDTO(bookDTO);

        if (bookDTO.getBookCategories() != null && bookDTO.getBookCategories().size() > 0) {
            book.getBookCategories().clear();

            for (CategoryDTO categoryDTO : bookDTO.getBookCategories()) {
                Category persistedCategory = categoryJpaRepository.findById(categoryDTO.getId()).orElse(null);
                if (persistedCategory != null) {
                    book.getBookCategories().add(persistedCategory);
                }
            }
        }
        return booksMapper.toDto(booksJpaRepository.save(book));
    }

    /**
     * Update book
     *
     * @param bookDTO bookDTO
     * @return updated bookDTO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public BookDTO update(Integer id, BookDTO bookDTO) {
        BookDTO savedBookDTO;
        Book persistedBook = booksJpaRepository.findById(id).orElse(null);

        if (persistedBook != null && bookDTO != null) {
            booksMapper.setBookParams(bookDTO, persistedBook);
            persistedBook.getBookCategories().clear();

            for (CategoryDTO cDTO : bookDTO.getBookCategories()) {
                Category category = categoryJpaRepository.findById(cDTO.getId()).orElse(null);
                persistedBook.getBookCategories().add(category);
            }

            savedBookDTO = booksMapper.toDto(booksJpaRepository.save(persistedBook));
        } else {
            throw new BookstoreException(messageSource.getMessage("error.no.book.found", null, LocaleContextHolder.getLocale()));
        }
        return savedBookDTO;
    }

    /**
     * Delete book by id
     *
     * @param id id
     * @return 0 when user not removed and 1 if user removed successfully
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Long delete(Integer id) {
        return booksJpaRepository.removeById(id);

    }
}
