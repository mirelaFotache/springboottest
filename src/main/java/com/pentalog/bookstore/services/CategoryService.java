package com.pentalog.bookstore.services;

import com.pentalog.bookstore.dto.CategoryDTO;
import com.pentalog.bookstore.dto.CategoryMapper;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Category;
import com.pentalog.bookstore.persistence.repositories.CategoryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    @Resource
    private CategoryJpaRepository categoryJpaRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * Find category by name
     * @param categoryName category name
     * @return categories
     */
    public Collection<CategoryDTO> findByCategoryName(String categoryName){
        return categoryJpaRepository.findByName(categoryName.toLowerCase()).stream().map(categoryMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Find categories
     * @return categories
     */
    public Collection<CategoryDTO> findAll() {
        return categoryJpaRepository.findAll().stream().map(categoryMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Insert new category
     * @param categoryDTO categoryDTO
     * @return inserted category
     */
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        return categoryMapper.toDto(categoryJpaRepository.save(categoryMapper.fromDto(categoryDTO)));
    }

    /**
     * Update category
     * @param id id
     * @param categoryDTO categoryDTO
     * @return updated category
     */
    public CategoryDTO update(Integer id, CategoryDTO categoryDTO) {
        Category persistedCategory = categoryJpaRepository.findById(id).orElse(null);

        if(persistedCategory!=null && categoryDTO!=null) {
            persistedCategory.setName(categoryDTO.getName());

            return categoryMapper.toDto(categoryJpaRepository.save(persistedCategory));
        }else{
            throw new BookstoreException("Category not found!");
        }
    }

    /**
     * Delete category by id
     * @param id id
     * @return return 0 when user not removed and 1 if user removed successfully
     */
    public Long delete(Integer id) {
        return categoryJpaRepository.removeById(id);
    }
}
