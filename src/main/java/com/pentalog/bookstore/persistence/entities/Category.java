package com.pentalog.bookstore.persistence.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    @Version
    private Integer version;

    @ManyToMany(mappedBy = "bookCategories", fetch = FetchType.LAZY)
    private List<Book> books;

    /**
     * Getter/setter section
     */

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
