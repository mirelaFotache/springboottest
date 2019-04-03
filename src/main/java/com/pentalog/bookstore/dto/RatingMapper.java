package com.pentalog.bookstore.dto;
import com.pentalog.bookstore.persistence.entities.Rating;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface RatingMapper {

    RatingDTO toRatingDTO(Rating rating);

    Collection<RatingDTO> toRatingDTOs(Collection<Rating> rating);

    Rating toRating(RatingDTO ratingDTO);
}
