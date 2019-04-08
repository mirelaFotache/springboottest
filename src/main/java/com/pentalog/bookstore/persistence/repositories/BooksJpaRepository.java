package com.pentalog.bookstore.persistence.repositories;

import com.pentalog.bookstore.persistence.entities.Book;
import com.pentalog.bookstore.persistence.entities.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface BooksJpaRepository extends CommonRepository<Book, Integer> {

    @Query("SELECT b FROM Book b where lower(b.title) like %:title%")
    Collection<Book> findByTitle(@Param("title") String title);

    @Query("SELECT b FROM Book b where lower(b.author) like %:author%")
    Collection<Book> findByAuthor(@Param("author") String author);

    @Query("SELECT b.bookCategories FROM Book b  where b.id=:id")
    Collection<Category> findBookCategories(@Param("id") Integer id);


    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.bookCategories as cb  LEFT JOIN Booking bg on b.id=bg.bookingBook.id where bg.startDate in (select max(bb.startDate) from Booking bb " +
            "where bg.bookingBook.id=bb.bookingBook.id) ")
    List<Book> findAllBooks();
}
