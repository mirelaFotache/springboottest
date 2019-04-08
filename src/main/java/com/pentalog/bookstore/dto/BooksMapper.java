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

    public Book fromDTO(BookDTO bookDTO){
        Book book = new Book();
        setParams(bookDTO,book);

        // Set categories associated with current book
        List<Category> bookCategories = new ArrayList<>();
        if (bookDTO.getBookCategories() != null) {
            for (CategoryDTO c : bookDTO.getBookCategories()) {
                Category category = new Category();
                category.setId(c.getId());
                category.setName(c.getName());
                bookCategories.add(category);
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

    public CategoryDTO toCategoryDto(Category c) {
        CategoryDTO category = new CategoryDTO();
        category.setId(c.getId());
        category.setName(c.getName());
        return category;
    }
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
            if(book.getBooking()!=null){
                BookingDTO bookingDTO = new BookingDTO();
                bookingDTO.setId(book.getBooking().getId());
                bookingDTO.setStartDate(book.getBooking().getStartDate());
                bookingDTO.setEstimatedEndDate(book.getBooking().getEstimatedEndDate());
                bookingDTO.setRealEndDate(book.getBooking().getRealEndDate());
                bookings.add(bookingDTO);
            }
            else {
                for (Booking booking : book.getBookings()) {
                    BookingDTO bookingDTO = new BookingDTO();
                    bookingDTO.setId(booking.getId());
                    bookingDTO.setStartDate(booking.getStartDate());
                    bookingDTO.setEstimatedEndDate(booking.getEstimatedEndDate());
                    bookingDTO.setRealEndDate(booking.getRealEndDate());
                    bookings.add(bookingDTO);
                }
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

    public void setParams(BookDTO bookDTO, Book book) {
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setBookNumber(bookDTO.getBookNumber());
        book.setBookImage(bookDTO.getBookImage());
        book.setPublishedDate(bookDTO.getPublishedDate());
        book.setBookLocation(bookDTO.getBookLocation());
    }

}
