package com.pentalog.bookstore.persistence.entities;

import com.pentalog.bookstore.utils.BookLocation;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue
    private int id;
    private String title;
    private String author;
    @Version
    private Integer version;
    @Column(name = "book_number")
    private int bookNumber;
    @Column(name = "book_image")
    private String bookImage;
    @Column(name = "published_date")
    private Date publishedDate;
    @Enumerated(EnumType.STRING)
    private BookLocation bookLocation;

    /**
     * Primary key constrain
     */

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "book_categories",
            joinColumns = @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "FK_CATEGORY_BOOK"), referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "FK_CATEGORY"), referencedColumnName = "id"))
    private List<Category> bookCategories;

    /**
     * Foreign key constrain
     */

    @OneToMany(mappedBy = "ratingBook", fetch = FetchType.LAZY)
    private List<Rating> ratings;
    @OneToMany(mappedBy = "bookingBook", fetch = FetchType.LAZY)
    private List<Booking> bookings;

    @Transient
    private Booking booking;

    /**
     * Getter/setter section
     */

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public List<Category> getBookCategories() {
        return bookCategories;
    }

    public void setBookCategories(List<Category> bookCategories) {
        this.bookCategories = bookCategories;
    }

    public BookLocation getBookLocation() {
        return bookLocation;
    }

    public void setBookLocation(BookLocation bookLocation) {
        this.bookLocation = bookLocation;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
