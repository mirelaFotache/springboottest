package com.pentalog.bookstore.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RatingDTO {

    private int id;
    private int rating;
    private String comment;
    private Date commentDate;

    private BookDTO ratingBook;
    private UserDTO ratingUser;

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

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public BookDTO getRatingBook() {
        return ratingBook;
    }

    public void setRatingBook(BookDTO ratingBook) {
        this.ratingBook = ratingBook;
    }

    public UserDTO getRatingUser() {
        return ratingUser;
    }

    public void setRatingUser(UserDTO ratingUser) {
        this.ratingUser = ratingUser;
    }
}
