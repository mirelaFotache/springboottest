package com.pentalog.bookstore.services;

import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Category;
import com.pentalog.bookstore.persistence.repositories.CategoryJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;

@Service
@Transactional
public class CategoryService {

    @Resource
    private CategoryJpaRepository categoryJpaRepository;

    /**
     * Find category by name
     * @param categoryName category name
     * @return categories
     */
    public Collection<Category> findByCategoryName(String categoryName){
        return categoryJpaRepository.findByName(categoryName.toLowerCase());
    }

    /**
     * Find categories
     * @return categories
     */
    public Collection<Category> findAll() {
        return categoryJpaRepository.findAll();
    }

    /**
     * Insert new category
     * @param category category
     * @return inserted category
     */
    public Category insert(Category category) {
        return categoryJpaRepository.save(category);
    }

    /**
     * Update category
     * @param id id
     * @param category category
     * @return updated category
     */
    public Category update(Integer id, Category category) {
        Category savedCategory = null;
        Category persistedCategory = categoryJpaRepository.findById(id).orElse(null);

        if(persistedCategory!=null && category!=null) {
            persistedCategory.setName(category.getName());

            savedCategory = categoryJpaRepository.save(persistedCategory);
        }else{
            throw new BookstoreException("Category not found!");
        }
        return savedCategory;
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
