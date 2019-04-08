package com.pentalog.bookstore.dto;

import com.pentalog.bookstore.persistence.entities.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    /**
     * Convert Category to CategoryDTO
     *
     * @param category category
     * @return categoryDTO
     */
    public CategoryDTO toDto(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    /**
     * Convert categoryDTO to category
     *
     * @param categoryDTO categoryDTO
     * @return category
     */
    public Category fromDto(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        return category;
    }
}
