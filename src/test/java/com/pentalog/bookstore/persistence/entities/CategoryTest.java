package com.pentalog.bookstore.persistence.entities;

import com.pentalog.bookstore.utils.BookLocation;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import static org.junit.Assert.*;

public class CategoryTest {

    private Category category=new Category();
    private List<Book> books = new ArrayList<>();
    @Before
    public void init(){

        category.setId(1);
        category.setName("programming");
        category.setVersion(1);

        Book book = new Book();
        book.setId(1);
        book.setAuthor("Goncalves");
        book.setBookLocation(BookLocation.FLOOR1);
        book.setBookImage("path to book image");
        book.setBookNumber(123456);
        book.setPublishedDate(new Date());
        book.setTitle("Beginning Java EE");
        books.add(book);
    }

    @Test
    public void getId() {
        assertEquals(1, category.getId());
    }

    @Test
    public void setId() {
        category.setId(2);
        assertEquals(2, category.getId());
    }

    @Test
    public void getName() {
        assertEquals("programming", category.getName());
    }

    @Test
    public void setName() {
        category.setName("science");
        assertEquals("science", category.getName());
    }

    @Test
    public void getVersion() {
        assertEquals(1,category.getVersion().intValue());
    }

    @Test
    public void setVersion() {
        category.setName("art");
        assertEquals(1,category.getVersion().intValue());
    }

    @Test
    public void getBooks() {
        category.setBooks(books);
        assertEquals(1,category.getBooks().size());
    }

    @Test
    public void setBooks() {
        category.setBooks(books);
        assertEquals(1,category.getBooks().size());
    }
}