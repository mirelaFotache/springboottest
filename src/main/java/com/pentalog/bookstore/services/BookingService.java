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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BookingService {

    private BookingJpaRepository bookingJpaRepository;
    private BookingsMapper bookingsMapper;
    private EntityManager em;
    private UserJpaRepository userJpaRepository;
    private BooksJpaRepository bookJpaRepository;
    @Autowired
    private YAMLConfig yamlConfig;


    public BookingService(BookingJpaRepository bookingJpaRepository, BookingsMapper bookingsMapper, EntityManager em, UserJpaRepository userJpaRepository, BooksJpaRepository bookJpaRepository) {
        this.bookingJpaRepository = bookingJpaRepository;
        this.bookingsMapper = bookingsMapper;
        this.em = em;
        this.userJpaRepository = userJpaRepository;
        this.bookJpaRepository = bookJpaRepository;
    }


    /**
     * Get all bookings
     *
     * @return bookings
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Collection<BookingDTO> findAll() {
        return bookingJpaRepository.findAll().stream().map(bookingsMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Find all bookings for given book
     *
     * @param bookId book id
     * @return all bookings for given book
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Collection<BookingDTO> findBookingsByBookId(Integer bookId) {
        return bookingJpaRepository.findBookingsByBookId(bookId).stream().map(bookingsMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * persist booking
     *
     * @param bookingDTO bookingDTO
     * @return persisted bookingDTO
     */
    public BookingDTO insert(BookingDTO bookingDTO) {
        UserDTO userDTO = bookingDTO.getBookingUser();
        BookDTO bookDTO = bookingDTO.getBookingBook();
        Booking booking = bookingsMapper.fromDTO(bookingDTO);

        //Check if user has an reservation on given book
        Booking lastBooking = getLastBooking(userDTO, bookDTO);

        //If lastBooking is null then no booking has been done for that book by the user with given id
        //If realEndDate is null then the book with given Id has not been returned yet
        if (lastBooking == null || lastBooking.getRealEndDate() != null) {

            findUserAndBookById(userDTO, bookDTO, booking);
            // If given user has more than N active bookings(where realEndDate is null) no other booking is allowed
            if (bookingJpaRepository.findBookingsByUserId(userDTO.getId()) < yamlConfig.getMaxBookingsAllowed()) {
                final Booking savedBooking = bookingJpaRepository.save(booking);

                return bookingsMapper.toDTO(savedBooking);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Booking not allowed. User has at least ");
                sb.append(yamlConfig.getMaxBookingsAllowed());
                sb.append(" active bookings");
                throw new BookstoreException(sb.toString());
            }
        } else {
            throw new BookstoreException("Booking was not saved because another booking is still active for this user...");
        }
    }

    /**
     * Find user and book by ID
     *
     * @param userDTO userDTO
     * @param bookDTO bookDTO
     * @param booking booking
     */
    public void findUserAndBookById(UserDTO userDTO, BookDTO bookDTO, Booking booking) {
        if (userDTO != null && userDTO.getId() > 0) {
            User persistedUser = userJpaRepository.findById(userDTO.getId()).orElse(null);
            booking.setBookingUser(persistedUser);
        } else {
            throw new BookstoreException("User not found");
        }

        if (bookDTO != null && bookDTO.getId() > 0) {
            Book persistedBook = bookJpaRepository.findById(bookDTO.getId()).orElse(null);
            booking.setBookingBook(persistedBook);
        } else {
            throw new BookstoreException("Book not found");
        }
    }

    /**
     * Find last booking for given book and user
     *
     * @param userDTO userDTO
     * @param bookDTO boookDTO
     * @return booking
     */
    private Booking getLastBooking(UserDTO userDTO, BookDTO bookDTO) {
        String hql = "SELECT b FROM Booking b where b.bookingBook.id=:bookId and b.bookingUser.id=:userId order by b.startDate desc";
        TypedQuery<Booking> query = em.createQuery(hql, Booking.class);
        query.setParameter("bookId", bookDTO.getId());
        query.setParameter("userId", userDTO.getId());
        query.setMaxResults(1);
        Booking lastBooking = null;
        try {
            lastBooking = query.getSingleResult();
        } catch (Exception e) {
            //Do nothing
        }
        return lastBooking;
    }

    /**
     * Update booking
     *
     * @param id         id
     * @param bookingDTO bookingDTO
     * @return updated bookingDTO
     */
    public BookingDTO update(Integer id, BookingDTO bookingDTO) {
        Booking persistedBooking = bookingJpaRepository.findById(id).orElse(null);

        if (persistedBooking != null && bookingDTO != null) {
            persistedBooking.setStartDate(bookingDTO.getStartDate());
            persistedBooking.setEstimatedEndDate(bookingDTO.getEstimatedEndDate());

            Book book = bookJpaRepository.getOne(bookingDTO.getBookingBook().getId());
            persistedBooking.setBookingBook(book);
            User user = userJpaRepository.findById(bookingDTO.getBookingUser().getId()).orElse(null);
            persistedBooking.setBookingUser(user);

            return bookingsMapper.toDTO(bookingJpaRepository.save(persistedBooking));
        } else {
            throw new BookstoreException("Booking not found");
        }
    }

    /**
     * Delete booking
     *
     * @param id id
     * @return 0 if booking not removed and 1 if booking removed successfully
     */
    public Long delete(Integer id) {
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
}
