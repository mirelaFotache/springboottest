package com.pentalog.bookstore.persistence.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name="estimated_end_date")
    private Date estimatedEndDate;
    @Column(name="real_end_date")
    private Date realEndDate;
    @Version
    private Integer version;

    /**
     * Primary key constrain
     */

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "FK_BOOKING_BOOK"), referencedColumnName = "id")
    private Book bookingBook;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_BOOKING_USER"),  referencedColumnName = "id")
    private User bookingUser;

    /**
     * Getter/setter section
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public User getBookingUser() {
        return bookingUser;
    }

    public void setBookingUser(User bookingUser) {
        this.bookingUser = bookingUser;
    }

    public Book getBookingBook() {
        return bookingBook;
    }

    public void setBookingBook(Book bookingBook) {
        this.bookingBook = bookingBook;
    }
}
