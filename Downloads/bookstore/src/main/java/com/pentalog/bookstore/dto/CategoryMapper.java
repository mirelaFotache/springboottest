package com.pentalog.bookstore.dto;

import com.pentalog.bookstore.persistence.entities.Category;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface  CategoryMapper {
    CategoryDTO toCategoryDTO(Category category);

    Collection<CategoryDTO> toCategoryDTOs(Collection<Category> categories);

    Category toCategory(CategoryDTO categoryDTO);

    List<Category> toCategories(List<CategoryDTO> categories);
}
