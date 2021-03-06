package com.pentalog.bookstore.controllers;

import com.pentalog.bookstore.dto.CategoryDTO;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Resource
    private CategoryService categoryService;
    @Autowired
    private MessageSource messageSource;

    /**
     * Find category by name
     * @param name name
     * @return categories by name
     */
    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public ResponseEntity<Collection<CategoryDTO>> findByName(@RequestParam("searchBy") String name) {
        return new ResponseEntity<>(categoryService.findByCategoryName(name), HttpStatus.OK);

    }

    /**
     * Find categories
     * @return all the categories
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<CategoryDTO>> getAllCategories() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    /**
     * Insert category
     * @param categoryDTO category
     * @return persisted category
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CategoryDTO> insertCategory(@RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(categoryService.insert(categoryDTO), HttpStatus.OK);
    }

    /**
     * Update category
     * @param categoryDTO category
     * @return updated category
     */
    @RequestMapping(value = "/{id}",  method = RequestMethod.PUT)
    public ResponseEntity<CategoryDTO> update(@PathVariable("id") Integer id,@RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(categoryService.update(id, categoryDTO), HttpStatus.OK);
    }

    /**
     * Delete category by id
     * @param id id
     * @return status message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable("id") Integer id) {
        final Long deleted = categoryService.delete(id);
        if (deleted != null && deleted.intValue() == 1)
            return new ResponseEntity<>(messageSource.getMessage("message.category.deleted",null, LocaleContextHolder.getLocale()), HttpStatus.NO_CONTENT);
        else
            throw new BookstoreException(messageSource.getMessage("error.no.category.found",null, LocaleContextHolder.getLocale()));
    }
}
