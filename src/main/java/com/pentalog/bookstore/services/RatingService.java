package com.pentalog.bookstore.services;

import com.pentalog.bookstore.dto.RatingDTO;
import com.pentalog.bookstore.dto.RatingMapper;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Book;
import com.pentalog.bookstore.persistence.entities.Rating;
import com.pentalog.bookstore.persistence.entities.User;
import com.pentalog.bookstore.persistence.repositories.BooksJpaRepository;
import com.pentalog.bookstore.persistence.repositories.RatingJpaRepository;
import com.pentalog.bookstore.persistence.repositories.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RatingService {

    @Autowired
    private RatingJpaRepository ratingJpaRepository;
    @Autowired
    private BooksJpaRepository booksJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private RatingMapper ratingMapper;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Collection<RatingDTO> findRatingsByUserId(Integer userId) {
        return ratingJpaRepository.findUserRatings(userId).stream().map(ratingMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Find all ratings
     *
     * @return all ratings
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Collection<RatingDTO> findAll() {
        return ratingJpaRepository.findAll().stream().map(ratingMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Persist rating
     *
     * @param ratingDTO ratingDTO
     * @return persisted ratingDTO
     */
    public RatingDTO insert(RatingDTO ratingDTO) {
        RatingDTO persistedRatingDTO;
        if (ratingDTO.getRatingUser() != null && ratingDTO.getRatingBook() != null) {

            User user = userJpaRepository.findById(ratingDTO.getRatingUser().getId()).orElse(null);
            Book book = booksJpaRepository.findById(ratingDTO.getRatingBook().getId()).orElse(null);
            Rating rating = new Rating();
            if(user!=null && book!=null) {
                rating.setComment(ratingDTO.getComment());
                rating.setCommentDate(ratingDTO.getCommentDate());
                rating.setRating(ratingDTO.getRating());
                rating.setRatingUser(user);
                rating.setRatingBook(book);
                ratingJpaRepository.save(rating);
                persistedRatingDTO = ratingMapper.toDto(rating);
            } else {
                throw new BookstoreException("User or book not found!");
            }
        } else {
            throw new BookstoreException("User or book are not found!");
        }

        return persistedRatingDTO;
    }

    /**
     * Update rating
     *
     * @param id        id
     * @param ratingDTO ratingDTO
     * @return updated ratingDTO
     */
    public RatingDTO update(Integer id, RatingDTO ratingDTO) {
        Rating persistedRating = ratingJpaRepository.findById(id).orElse(null);

        if (persistedRating != null && ratingDTO != null) {
            persistedRating.setComment(ratingDTO.getComment());
            persistedRating.setCommentDate(ratingDTO.getCommentDate());

            if(ratingDTO.getRatingUser()!=null && ratingDTO.getRatingBook()!=null){
                User user = userJpaRepository.findById(ratingDTO.getRatingUser().getId()).orElse(null);
                persistedRating.setRatingUser(user);
                Book book = booksJpaRepository.findById(ratingDTO.getRatingBook().getId()).orElse(null);
                persistedRating.setRatingBook(book);
            }else{
                throw new BookstoreException("User or book not found!");
            }

            return ratingMapper.toDto(ratingJpaRepository.save(persistedRating));
        } else {
            throw new BookstoreException("Rating not found!");
        }
    }

    /**
     * Delete rating
     *
     * @param id id
     * @return 0 when rating was not removed and 1 if rating was removed successfully
     */
    public Long delete(Integer id) {
        return ratingJpaRepository.removeById(id);
    }
}
