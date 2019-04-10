package com.pentalog.bookstore.dto;

import org.mapstruct.Mapper;

import java.util.Collection;
import com.pentalog.bookstore.persistence.entities.Book;

@Mapper
public interface BooksMapper {

    BooksDTO toBooksDTO(Book book);

    Collection<BooksDTO> toBooksDTOs(Collection<Book> books);

    Book toBooks(BooksDTO booksDTO);
}
