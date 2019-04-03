package com.pentalog.bookstore.dto;

import com.pentalog.bookstore.persistence.entities.Book;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface BookMapper {

    BookDTO toBookDTO(Book book);

    Collection<BookDTO> toBookDTOs(Collection<Book> books);

    Book toBook(BookDTO booksDTO);
}
