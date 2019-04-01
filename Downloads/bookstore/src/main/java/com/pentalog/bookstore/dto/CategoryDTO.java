package com.pentalog.bookstore.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO {

    private int id;
    private String name;
    private List<BooksDTO> books;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BooksDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BooksDTO> books) {
        this.books = books;
    }
}
