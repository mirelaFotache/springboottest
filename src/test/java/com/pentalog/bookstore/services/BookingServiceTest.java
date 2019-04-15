package com.pentalog.bookstore.services;

import com.pentalog.bookstore.dto.BookingDTO;
import com.pentalog.bookstore.dto.BookingsMapper;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Booking;
import com.pentalog.bookstore.persistence.repositories.BookingJpaRepository;
import com.pentalog.bookstore.persistence.repositories.BooksJpaRepository;
import com.pentalog.bookstore.persistence.repositories.UserJpaRepository;
import com.pentalog.bookstore.utils.BookingSupplier;
import com.pentalog.bookstore.utils.UserSupplier;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserJpaRepository.class)
public class BookingServiceTest {

    private static String HQL_SELECT_BOOKING = "SELECT b FROM Booking b where b.bookingBook.id=:bookId and b.bookingUser.id=:userId order by b.startDate desc";

    private BookingService bookingService = new BookingService(Mockito.mock(BookingJpaRepository.class), Mockito.mock(BookingsMapper.class),
            Mockito.mock(EntityManager.class), Mockito.mock(UserJpaRepository.class), Mockito.mock(BooksJpaRepository.class), null);

    private BookingJpaRepository bookingJpaRepository = bookingService.getBookingJpaRepository();
    private BookingsMapper bookingsMapper = bookingService.getBookingsMapper();
    private EntityManager em = bookingService.getEm();
    private UserJpaRepository userJpaRepository = bookingService.getUserJpaRepository();
    private BooksJpaRepository bookJpaRepository = bookingService.getBookJpaRepository();

    //@Test
    public void findAll() {
    }

    //@Test
    public void findBookingsByBookId() {
    }

    @Test
    public void insertNotAllowed() {
        final Booking suppliedBooking = BookingSupplier.supplyBookingForInsertNotAllowed();
        final BookingDTO suppliedBookingDto = BookingSupplier.supplyBookingDTOForInsertNotAllowed();
        MessageSource messageSource = Mockito.mock(MessageSource.class);
        bookingService.setMessageSource(messageSource);
        Mockito.when(messageSource.getMessage("error.book.not.available", null, LocaleContextHolder.getLocale())).thenReturn("No user found!");

        testBookingNotAllowed(suppliedBooking, suppliedBookingDto);
    }

    @Test
    public void insertBookingNotAllowedUserIdZero() {
        final Booking suppliedBooking = BookingSupplier.supplyBookingForInsertNotAllowedUserIdZero();
        final BookingDTO suppliedBookingDto = BookingSupplier.supplyBookingDTOForInsertNotAllowedUserIdZero();

        testBookingNotAllowed(suppliedBooking, suppliedBookingDto);
    }

    @Test
    public void insertBookingNotAllowedBookIdZero() {
        final Booking suppliedBooking = BookingSupplier.supplyBookingForInsertNotAllowedBookIdZero();
        final BookingDTO suppliedBookingDto = BookingSupplier.supplyBookingDTOForInsertNotAllowedBookIdZero();
        Mockito.doReturn(UserSupplier.supplyUserForInsert()).when(userJpaRepository).findById(suppliedBooking.getBookingUser().getId());
        testBookingNotAllowed(suppliedBooking, suppliedBookingDto);
    }

    @Test
    public void insertNotAllowedStockInvalid() {
        final Booking suppliedBooking = BookingSupplier.supplyBookingForInsertNoAvailableStock();
        final BookingDTO suppliedBookingDto = BookingSupplier.supplyBookingDTOForInsertNoAvailableStock();

        mockBookingJpaAndMapper(suppliedBooking, suppliedBookingDto);
        Mockito.doReturn(suppliedBookingDto).when(bookingsMapper).toDTO(suppliedBooking);
        TypedQuery<Booking> mockedQuery = Mockito.mock(TypedQuery.class);

        Mockito.doReturn(mockedQuery).when(em).createQuery(HQL_SELECT_BOOKING, Booking.class);
        Mockito.when(mockedQuery.getSingleResult()).thenReturn(suppliedBooking);

        Mockito.doReturn(UserSupplier.supplyUserForInsert()).when(userJpaRepository).findById(suppliedBooking.getBookingUser().getId());
        Mockito.doReturn(BookingSupplier.supplyBookForInsert()).when(bookJpaRepository).findById(suppliedBooking.getBookingBook().getId());

        MessageSource messageSource = Mockito.mock(MessageSource.class);
        bookingService.setMessageSource(messageSource);
        Mockito.when(messageSource.getMessage("error.book.not.available", null, LocaleContextHolder.getLocale())).thenReturn("Book not available!");

        Assert.assertThrows(BookstoreException.class, () ->
                getInsert(suppliedBookingDto)
        );
    }

    @Test
    public void insertNotAllowedBookNotAvailable() {
        final Booking suppliedBooking = BookingSupplier.supplyBookingForInsertAllowed();
        final BookingDTO suppliedBookingDto = BookingSupplier.supplyBookingDTOForInsertAllowed();

        mockBookingJpaAndMapper(suppliedBooking, suppliedBookingDto);
        Mockito.doReturn(suppliedBookingDto).when(bookingsMapper).toDTO(suppliedBooking);
        TypedQuery<Booking> mockedQuery = Mockito.mock(TypedQuery.class);

        Mockito.doReturn(mockedQuery).when(em).createQuery(HQL_SELECT_BOOKING, Booking.class);
        Mockito.when(mockedQuery.getSingleResult()).thenThrow(NoResultException.class);
        MessageSource messageSource = Mockito.mock(MessageSource.class);
        bookingService.setMessageSource(messageSource);
        Mockito.when(messageSource.getMessage("error.book.not.available", null, LocaleContextHolder.getLocale())).thenReturn("Book not available!");

        Assert.assertThrows(BookstoreException.class, () ->
                getInsert(suppliedBookingDto)
        );
    }

    //@Test
    public void update() {
    }

    //@Test
    public void delete() {
    }

    /**
     * Mock bookingJpaRepository and BookingMapper
     *
     * @param suppliedBooking    supplied booking
     * @param suppliedBookingDto supplied bookingDTO
     */
    private void mockBookingJpaAndMapper(Booking suppliedBooking, BookingDTO suppliedBookingDto) {
        Mockito.doReturn(suppliedBooking).when(bookingJpaRepository).save(suppliedBooking);
        Mockito.doReturn(suppliedBooking).when(bookingsMapper).fromDTO(suppliedBookingDto);
    }

    /**
     * Test booking not allowed
     * @param suppliedBooking supplied booking
     * @param suppliedBookingDto supplied bookingDTO
     */
    private void testBookingNotAllowed(Booking suppliedBooking, BookingDTO suppliedBookingDto) {
        mockBookingJpaAndMapper(suppliedBooking, suppliedBookingDto);

        TypedQuery<Booking> mockedQuery = Mockito.mock(TypedQuery.class);

        Mockito.doReturn(mockedQuery).when(em).createQuery(HQL_SELECT_BOOKING, Booking.class);
        Mockito.when(mockedQuery.getSingleResult()).thenReturn(suppliedBooking);

        MessageSource messageSource = Mockito.mock(MessageSource.class);
        bookingService.setMessageSource(messageSource);
        Mockito.when(messageSource.getMessage("error.no.book.found", null, LocaleContextHolder.getLocale())).thenReturn("No book found!");

        Assert.assertThrows(BookstoreException.class, () ->
                getInsert(suppliedBookingDto)
        );
    }

    private BookingDTO getInsert(BookingDTO suppliedBookingDto) {
        return bookingService.insert(suppliedBookingDto);
    }
}