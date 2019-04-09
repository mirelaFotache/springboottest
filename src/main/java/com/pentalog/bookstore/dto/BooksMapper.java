package com.pentalog.bookstore.dto;

import com.pentalog.bookstore.persistence.entities.Book;
import com.pentalog.bookstore.persistence.entities.Booking;
import com.pentalog.bookstore.persistence.entities.Category;
import com.pentalog.bookstore.persistence.entities.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BooksMapper {

    @Autowired
    private BookingsMapper bookingsMapper;

    /**
     * Convert bookDTO to book
     *
     * @param bookDTO bookDTO
     * @return book
     */
    public Book fromDTO(BookDTO bookDTO) {
        Book book = new Book();
        setBookParams(bookDTO, book);

        // Set categories associated with current book
        List<Category> bookCategories = new ArrayList<>();
        if (bookDTO.getBookCategories() != null) {
            for (CategoryDTO categoryDTO : bookDTO.getBookCategories()) {
                bookCategories.add(toCategory(categoryDTO));
            }
            book.setBookCategories(bookCategories);
        }

        // Set bookings associated with current book
        List<Booking> bookings = new ArrayList<>();
        if (book.getBookings() != null) {
            for (BookingDTO bookingDTO : bookDTO.getBookings()) {
                bookings.add(bookingsMapper.fromDTO(bookingDTO));
            }
            book.setBookings(bookings);
        }

        // Set ratings associated with current book
        List<Rating> ratings = new ArrayList<>();
        if (book.getRatings() != null) {
            for (RatingDTO r : bookDTO.getRatings()) {
                Rating rating = new Rating();
                rating.setId(r.getId());
                rating.setComment(r.getComment());
                rating.setCommentDate(r.getCommentDate());
                ratings.add(rating);
            }
            book.setRatings(ratings);
        }

        return book;
    }

    /**
     * Convert book to bookDTO
     *
     * @param book book
     * @return bookDTO
     */
    public BookDTO toDto(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setBookNumber(book.getBookNumber());
        bookDTO.setBookImage(book.getBookImage());
        bookDTO.setPublishedDate(book.getPublishedDate());
        bookDTO.setBookLocation(book.getBookLocation());

        // Set categories associated with current book
        List<CategoryDTO> bookCategories = new ArrayList<>();
        if (book.getBookCategories() != null) {
            for (Category c : book.getBookCategories()) {
                CategoryDTO category = new CategoryDTO();
                category.setId(c.getId());
                category.setName(c.getName());
                bookCategories.add(category);
            }
            bookDTO.setBookCategories(bookCategories);
        }

        // Set bookings associated with current book
        List<BookingDTO> bookings = new ArrayList<>();
        if (book.getBookings() != null) {
            for (Booking booking : book.getBookings()) {
                BookingDTO bookingDTO = new BookingDTO();
                bookingDTO.setId(booking.getId());
                bookingDTO.setStartDate(booking.getStartDate());
                bookingDTO.setEstimatedEndDate(booking.getEstimatedEndDate());
                bookingDTO.setRealEndDate(booking.getRealEndDate());
                bookings.add(bookingDTO);
            }
            bookDTO.setBookings(bookings);
        }

        // Set ratings associated with current book
        List<RatingDTO> ratings = new ArrayList<>();
        if (book.getRatings() != null) {
            for (Rating r : book.getRatings()) {
                RatingDTO rating = new RatingDTO();
                rating.setId(r.getId());
                rating.setComment(r.getComment());
                rating.setCommentDate(r.getCommentDate());
                ratings.add(rating);
            }
            bookDTO.setRatings(ratings);
        }

        return bookDTO;
    }

    /**
     * convert available book to available bookDTO
     *
     * @param book book
     * @return bookDTO
     */
    public BookDTO toAvailableBookDto(Book book) {

        // Set bookDTO parameters
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setBookNumber(book.getBookNumber());
        bookDTO.setBookImage(book.getBookImage());
        bookDTO.setPublishedDate(book.getPublishedDate());
        bookDTO.setBookLocation(book.getBookLocation());

        // Set categories associated with current book
        List<CategoryDTO> bookCategories = new ArrayList<>();
        if (book.getBookCategories() != null) {
            for (Category category : book.getBookCategories()) {
                bookCategories.add(toCategoryDto(category));
            }
            bookDTO.setBookCategories(bookCategories);
        }

        // Set available bookingDTO
        if (book.getBooking() != null) {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(book.getBooking().getId());
            bookingDTO.setStartDate(book.getBooking().getStartDate());
            bookingDTO.setEstimatedEndDate(book.getBooking().getEstimatedEndDate());
            bookingDTO.setRealEndDate(book.getBooking().getRealEndDate());
            bookDTO.setAvailableBooking(bookingDTO);
        }

        return bookDTO;
    }

    /**
     * Set book's parameters
     *
     * @param bookDTO bookDTO
     * @param book book
     */
    public void setBookParams(BookDTO bookDTO, Book book) {
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setBookNumber(bookDTO.getBookNumber());
        book.setBookImage(bookDTO.getBookImage());
        book.setPublishedDate(bookDTO.getPublishedDate());
        book.setBookLocation(bookDTO.getBookLocation());
    }

    /**
     * Convert category to CategoryDTO
     *
     * @param category category
     * @return categoryDTO
     */
    public CategoryDTO toCategoryDto(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    /**
     * Convert categoryDTO to category
     *
     * @param categoryDTO categoryDTO
     * @return category
     */
    private Category toCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        return category;
    }

}
