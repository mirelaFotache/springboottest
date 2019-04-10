package com.pentalog.bookstore.dto;

import com.pentalog.bookstore.persistence.entities.Rating;
import com.pentalog.bookstore.persistence.entities.User;
import com.pentalog.bookstore.persistence.entities.Book;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {

    /**
     * Convert rating to ratingDTO
     *
     * @param rating rating
     * @return ratingDTO
     */
    public RatingDTO toDto(Rating rating) {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setId(rating.getId());
        ratingDTO.setComment(rating.getComment());
        ratingDTO.setCommentDate(rating.getCommentDate());
        ratingDTO.setRating(rating.getRating());

        if (rating.getRatingUser() != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(rating.getRatingUser().getId());
            ratingDTO.setRatingUser(userDTO);
        }

        if(rating.getRatingBook()!=null){
            BookDTO bookDTO = new BookDTO();
            bookDTO.setId(rating.getRatingBook().getId());
            ratingDTO.setRatingBook(bookDTO);
        }

        return ratingDTO;
    }

    /**
     * Convert ratingDTO to rating
     *
     * @param ratingDTO ratingDTO
     * @return rating
     */
    public Rating fromDto(RatingDTO ratingDTO) {
        Rating rating = new Rating();
        rating.setId(ratingDTO.getId());
        rating.setRating(ratingDTO.getRating());
        rating.setComment(ratingDTO.getComment());
        rating.setCommentDate(ratingDTO.getCommentDate());

        if (ratingDTO.getRatingUser() != null) {
            User user = new User();
            user.setId(ratingDTO.getRatingUser().getId());
            rating.setRatingUser(user);
        }

        if(ratingDTO.getRatingBook()!=null){
            Book book = new Book();
            book.setId(ratingDTO.getRatingBook().getId());
            rating.setRatingBook(book);
        }

        return rating;
    }

}
