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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BookService {

    public static final String BOOK_NOT_FOUND = "Book not found!";
    @Autowired
    private BooksJpaRepository booksJpaRepository;
    @Autowired
    private CategoryJpaRepository categoryJpaRepository;
    @Autowired
    private BooksMapper booksMapper;
    @Autowired
    private EntityManager em;

    /**
     * Find categories asociated with given book
     *
     * @param bookId bookId
     * @return Categories
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Collection<CategoryDTO> findCategoriesByBookId(Integer bookId) {
        return booksJpaRepository.findBookCategories(bookId).stream().map(booksMapper::toCategoryDto).collect(Collectors.toList());
    }

    /**
     * Find all books. For each book display the most recent booking if any was found
     *
     * @return books list
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Collection<BookDTO> findBooksAvailability() {
        final List<Book> allBooks = booksJpaRepository.findAllBooks();
        for (Book book : allBooks) {
            String hql = "select b from Booking b where b.bookingBook.id=:id order by b.startDate desc";
            TypedQuery<Booking> query = em.createQuery(hql, Booking.class);
            query.setParameter("id", book.getId());
            query.setMaxResults(1);
            Booking booking = query.getSingleResult();
            book.setBooking(booking);
        }
        final List<BookDTO> bookDTOS = allBooks.stream().map(booksMapper::toAvailableBookDto).collect(Collectors.toList());
        return bookDTOS;

    }

    /**
     * Find books by title
     *
     * @param title title
     * @return books list
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Collection<BookDTO> findByTitle(String title) {
        return booksJpaRepository.findByTitle(title.toLowerCase()).stream().map(booksMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Find book by id
     *
     * @param id id
     * @return book by id
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public BookDTO findById(Integer id) {
        return booksMapper.toDto(booksJpaRepository.findById(id).orElse(null));
    }

    /**
     * Find books by author
     *
     * @param author author
     * @return books list
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Collection<BookDTO> findByAuthor(String author) {
        return booksJpaRepository.findByAuthor(author.toLowerCase()).stream().map(booksMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Save book
     *
     * @param bookDTO bookDTO
     * @return bookDTO
     */
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
            throw new BookstoreException(BOOK_NOT_FOUND);
        }
        return savedBookDTO;
    }

    /**
     * Delete book by id
     *
     * @param id id
     * @return 0 when user not removed and 1 if user removed successfully
     */
    public Long delete(Integer id) {
        return booksJpaRepository.removeById(id);

    }
}
