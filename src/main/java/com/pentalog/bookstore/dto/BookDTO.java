package com.pentalog.bookstore.dto;

import com.pentalog.bookstore.utils.BookLocation;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BookDTO {

    private int id;
    private String title;
    private String author;
    private int bookNumber;
    private String bookImage;
    private Date publishedDate;
    private BookLocation bookLocation;

    private List<CategoryDTO> bookCategories;
    private List<BookingDTO> bookings;
    private List<RatingDTO> ratings;

    public List<BookingDTO> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingDTO> bookings) {
        this.bookings = bookings;
    }

    public List<RatingDTO> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingDTO> ratings) {
        this.ratings = ratings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(int bookNumber) {
        this.bookNumber = bookNumber;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<CategoryDTO> getBookCategories() {
        return bookCategories;
    }

    public void setBookCategories(List<CategoryDTO> bookCategories) {
        this.bookCategories = bookCategories;
    }

    public BookLocation getBookLocation() {
        return bookLocation;
    }

    public void setBookLocation(BookLocation bookLocation) {
        this.bookLocation = bookLocation;
    }

}
