package com.pentalog.bookstore.dto;

import com.pentalog.bookstore.persistence.entities.Book;
import com.pentalog.bookstore.persistence.entities.Booking;
import com.pentalog.bookstore.persistence.entities.Category;
import com.pentalog.bookstore.persistence.entities.Rating;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BooksMapper {

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
            for (Booking b : book.getBookings()) {
                BookingDTO booking = new BookingDTO();
                booking.setId(b.getId());
                booking.setStartDate(b.getStartDate());
                booking.setEstimatedEndDate(b.getEstimatedEndDate());
                booking.setRealEndDate(b.getRealEndDate());
                bookings.add(booking);
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
}
