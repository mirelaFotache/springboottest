package com.pentalog.bookstore.services;

import com.pentalog.bookstore.dto.BookDTO;
import com.pentalog.bookstore.dto.BookingDTO;
import com.pentalog.bookstore.dto.BookingsMapper;
import com.pentalog.bookstore.dto.UserDTO;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Book;
import com.pentalog.bookstore.persistence.entities.Booking;
import com.pentalog.bookstore.persistence.entities.User;
import com.pentalog.bookstore.persistence.repositories.BookingJpaRepository;
import com.pentalog.bookstore.persistence.repositories.BooksJpaRepository;
import com.pentalog.bookstore.persistence.repositories.UserJpaRepository;
import com.pentalog.bookstore.utils.YAMLConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class BookingService {

    @Autowired
    private MessageSource messageSource;

    private BookingJpaRepository bookingJpaRepository;
    private BookingsMapper bookingsMapper;
    private EntityManager em;
    private UserJpaRepository userJpaRepository;
    private BooksJpaRepository bookJpaRepository;

    private YAMLConfig yamlConfig;

    public BookingService(BookingJpaRepository bookingJpaRepository,
                          BookingsMapper bookingsMapper,
                          EntityManager em,
                          UserJpaRepository userJpaRepository,
                          BooksJpaRepository bookJpaRepository,
                          YAMLConfig yamlConfig) {
        this.bookingJpaRepository = bookingJpaRepository;
        this.bookingsMapper = bookingsMapper;
        this.em = em;
        this.userJpaRepository = userJpaRepository;
        this.bookJpaRepository = bookJpaRepository;
        this.yamlConfig = yamlConfig;
    }

    /**
     * Get all bookings
     *
     * @return bookings
     */
    public Collection<BookingDTO> findAll() {
        return bookingJpaRepository.findAll().stream().map(bookingsMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Find all bookings for given book
     *
     * @param bookId book id
     * @return all bookings for given book
     */
    public Collection<BookingDTO> findBookingsByBookId(Integer bookId) {
        return bookingJpaRepository.findBookingsByBookId(bookId).stream().map(bookingsMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * persist booking
     *
     * @param bookingDTO bookingDTO
     * @return persisted bookingDTO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public BookingDTO insert(BookingDTO bookingDTO) {
        UserDTO userDTO = bookingDTO.getBookingUser();
        BookDTO bookDTO = bookingDTO.getBookingBook();
        Booking booking = bookingsMapper.fromDTO(bookingDTO);

        findUserAndBookById(userDTO, bookDTO, booking);

        // If given user has more than N active bookings(where realEndDate is null) no other booking is allowed
        final Collection<Booking> bookingsPerUser = bookingJpaRepository.getActiveBookingsByUserId(userDTO.getId());
        if (bookingsPerUser.size() < yamlConfig.getMaxAllowed()) {
            //Persist booking and update stock of available books
            if (booking.getRealEndDate() == null) {
                int availableBookStock = booking.getBookingBook().getStockAvailableBook();
                final int stockAvailableBook = availableBookStock - 1;
                if (stockAvailableBook < 0) {
                    throw new BookstoreException(messageSource.getMessage("error.update.stock.not.allowed", null, LocaleContextHolder.getLocale()));
                }
                booking.getBookingBook().setStockAvailableBook(stockAvailableBook);
                int counter = 0;
                for (Booking activeBooking : bookingsPerUser) {
                    if (activeBooking.getBookingBook().getId() == booking.getBookingBook().getId()) {
                        counter++;
                    }
                }
                if (counter > 0) {
                    throw new BookstoreException(messageSource.getMessage("error.user.has.active.booking", null, LocaleContextHolder.getLocale()));
                }
            }
            final Booking savedBooking = bookingJpaRepository.save(booking);

            return bookingsMapper.toDTO(savedBooking);
        } else {
            Object[] params = new Object[1];
            params[0] = yamlConfig.getMaxAllowed();
            throw new BookstoreException(messageSource.getMessage("error.booking.not.allowed.user.has.active.bookings", params, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * Find user and book by ID
     *
     * @param userDTO userDTO
     * @param bookDTO bookDTO
     * @param booking booking
     */

    private void findUserAndBookById(UserDTO userDTO, BookDTO bookDTO, Booking booking) {
        if (userDTO != null && userDTO.getId() > 0) {
            User persistedUser = userJpaRepository.findById(userDTO.getId()).orElse(null);
            booking.setBookingUser(persistedUser);
        } else {
            throw new BookstoreException("User not found");
        }

        if (bookDTO != null && bookDTO.getId() > 0) {
            Book persistedBook = bookJpaRepository.findById(bookDTO.getId()).orElse(null);
            if (persistedBook != null && persistedBook.getStockAvailableBook() > 0) {
                booking.setBookingBook(persistedBook);
            } else {
                throw new BookstoreException(messageSource.getMessage("error.book.not.available", null, LocaleContextHolder.getLocale()));
            }
        } else {
            throw new BookstoreException(messageSource.getMessage("error.no.book.found", null, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * Update booking
     *
     * @param id         id
     * @param bookingDTO bookingDTO
     * @return updated bookingDTO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public BookingDTO update(Integer id, BookingDTO bookingDTO) {
        Booking persistedBooking = bookingJpaRepository.findById(id).orElse(null);

        if (persistedBooking != null && bookingDTO != null) {
            //Set book
            Book book = bookJpaRepository.getOne(bookingDTO.getBookingBook().getId());
            setStockAvailableBook(bookingDTO, persistedBooking, book);
            persistedBooking.setBookingBook(book);

            //Set user
            User user = userJpaRepository.findById(bookingDTO.getBookingUser().getId()).orElse(null);
            persistedBooking.setBookingUser(user);

            //Update realEndDate only after calculating stockAvailableBook value!
            setBookingParameters(bookingDTO, persistedBooking);

            //Persist booking
            return bookingsMapper.toDTO(bookingJpaRepository.save(persistedBooking));
        } else {
            throw new BookstoreException(messageSource.getMessage("error.no.booking.found", null, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * Set stockAvailableBook parameter in book
     *
     * @param bookingDTO       bookingDTO
     * @param persistedBooking booking
     * @param book             book
     */
    private void setStockAvailableBook(BookingDTO bookingDTO, Booking persistedBooking, Book book) {
        boolean increaseBookStock = false;
        boolean decreaseBookStock = false;
        if (persistedBooking.getRealEndDate() == null && bookingDTO.getRealEndDate() != null) {
            increaseBookStock = true;
        } else if (persistedBooking.getRealEndDate() != null && bookingDTO.getRealEndDate() == null) {
            decreaseBookStock = true;
        }

        int stockAvailableBook = book.getStockAvailableBook();

        if (increaseBookStock) {
            stockAvailableBook = book.getStockAvailableBook() + 1;
            if (stockAvailableBook > yamlConfig.getMaxAllowed()) {
                throw new BookstoreException(messageSource.getMessage("error.update.stock.not.allowed", null, LocaleContextHolder.getLocale()));
            }

        } else if (decreaseBookStock) {
            stockAvailableBook = book.getStockAvailableBook() - 1;
            if (stockAvailableBook < 0) {
                throw new BookstoreException(messageSource.getMessage("error.update.stock.not.allowed", null, LocaleContextHolder.getLocale()));
            }
        }
        book.setStockAvailableBook(stockAvailableBook);
    }

    /**
     * Set startDate, estimatedEndDate and realEndDate parameters
     *
     * @param bookingDTO       bookingDTO
     * @param persistedBooking booking
     */
    private void setBookingParameters(BookingDTO bookingDTO, Booking persistedBooking) {
        persistedBooking.setStartDate(bookingDTO.getStartDate());
        persistedBooking.setEstimatedEndDate(bookingDTO.getEstimatedEndDate());
        persistedBooking.setRealEndDate(bookingDTO.getRealEndDate());
    }

    /**
     * Delete booking
     *
     * @param id id
     * @return 0 if booking not removed and 1 if booking removed successfully
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Long delete(Integer id) {
        Booking persistedBooking = bookingJpaRepository.findById(id).orElse(null);
        if (persistedBooking != null) {
            Book book = bookJpaRepository.getOne(persistedBooking.getBookingBook().getId());
            final int stockAvailableBook = book.getStockAvailableBook() + 1;
            if (stockAvailableBook > yamlConfig.getMaxAllowed()) {
                throw new BookstoreException(messageSource.getMessage("error.update.stock.not.allowed", null, LocaleContextHolder.getLocale()));
            }
            book.setStockAvailableBook(stockAvailableBook);
            bookJpaRepository.save(book);
        }
        return bookingJpaRepository.removeById(id);
    }

    public BookingJpaRepository getBookingJpaRepository() {
        return bookingJpaRepository;
    }

    public BookingsMapper getBookingsMapper() {
        return bookingsMapper;
    }

    public EntityManager getEm() {
        return em;
    }

    public UserJpaRepository getUserJpaRepository() {
        return userJpaRepository;
    }

    public BooksJpaRepository getBookJpaRepository() {
        return bookJpaRepository;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
