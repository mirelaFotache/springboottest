package com.pentalog.bookstore.persistence.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue
    private int id;
    private int rating;
    private String comment;
    private Date commentDate;
    @Version
    private Integer version;

    /**
     * Primary key constrain
     */

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "FK_RATING_BOOK"), referencedColumnName = "id")
    private Book ratingBook;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_RATING_USER"), referencedColumnName = "id")
    private User ratingUser;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public Book getRatingBook() {
        return ratingBook;
    }

    public void setRatingBook(Book ratingBook) {
        this.ratingBook = ratingBook;
    }

    public User getRatingUser() {
        return ratingUser;
    }

    public void setRatingUser(User ratingUser) {
        this.ratingUser = ratingUser;
    }
}
