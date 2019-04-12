package com.pentalog.bookstore.services;

import com.pentalog.bookstore.dto.CategoryDTO;
import com.pentalog.bookstore.dto.CategoryMapper;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Category;
import com.pentalog.bookstore.persistence.repositories.CategoryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CategoryService {

    @Resource
    private CategoryJpaRepository categoryJpaRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private MessageSource messageSource;

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
    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        return categoryMapper.toDto(categoryJpaRepository.save(categoryMapper.fromDto(categoryDTO)));
    }

    /**
     * Update category
     * @param id id
     * @param categoryDTO categoryDTO
     * @return updated category
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryDTO update(Integer id, CategoryDTO categoryDTO) {
        Category persistedCategory = categoryJpaRepository.findById(id).orElse(null);

        if(persistedCategory!=null && categoryDTO!=null) {
            persistedCategory.setName(categoryDTO.getName());

            return categoryMapper.toDto(categoryJpaRepository.save(persistedCategory));
        }else{
            throw new BookstoreException(messageSource.getMessage("error.no.category.found", null, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * Delete category by id
     * @param id id
     * @return return 0 when user not removed and 1 if user removed successfully
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Long delete(Integer id) {
        return categoryJpaRepository.removeById(id);
    }
}
