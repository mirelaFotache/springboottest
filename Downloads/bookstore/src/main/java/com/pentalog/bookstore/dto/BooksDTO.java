package com.pentalog.bookstore.dto;

import com.pentalog.bookstore.persistence.entities.Cart;
import com.pentalog.bookstore.persistence.entities.Category;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BooksDTO {

    private int id;
    private String title;
    private String author;
    private float price;
    private int bookNumber;
    private String bookImage;
    private Date publishedDate;
    private List<CartDTO> carts;
    private List<CategoryDTO> bookCategories;

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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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

    public List<CartDTO> getCarts() {
        return carts;
    }

    public void setCarts(List<CartDTO> carts) {
        this.carts = carts;
    }

    public List<CategoryDTO> getBookCategories() {
        return bookCategories;
    }

    public void setBookCategories(List<CategoryDTO> bookCategories) {
        this.bookCategories = bookCategories;
    }
}
