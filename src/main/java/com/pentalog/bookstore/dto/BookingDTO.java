package com.pentalog.bookstore.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BookingDTO {

    private int id;
    private Date startDate;
    private Date estimatedEndDate;
    private Date realEndDate;
    private BookDTO bookingBook;
    private UserDTO bookingUser;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEstimatedEndDate() {
        return estimatedEndDate;
    }

    public void setEstimatedEndDate(Date estimatedEndDate) {
        this.estimatedEndDate = estimatedEndDate;
    }

    public Date getRealEndDate() {
        return realEndDate;
    }

    public void setRealEndDate(Date realEndDate) {
        this.realEndDate = realEndDate;
    }

    public BookDTO getBookingBook() {
        return bookingBook;
    }

    public void setBookingBook(BookDTO bookingBook) {
        this.bookingBook = bookingBook;
    }

    public UserDTO getBookingUser() {
        return bookingUser;
    }

    public void setBookingUser(UserDTO bookingUser) {
        this.bookingUser = bookingUser;
    }
}
