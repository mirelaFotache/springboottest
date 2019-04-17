package com.pentalog.bookstore.controllers;

import com.pentalog.bookstore.dto.UserDTO;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;

@Slf4j

@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    private UserService userService;
    //private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private MessageSource messageSource;

    /**
     * Find user by user name
     *
     * @param userName user name
     * @return users
     */
    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> findByUserName(@RequestParam("searchBy") String userName) {
        final UserDTO user = userService.findByUserName(userName);
        if (user != null )
            return new ResponseEntity<>(user, HttpStatus.OK);
        else {
            throw new BookstoreException(messageSource.getMessage("error.no.user.found", null, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * Get user by id
     *
     * @param id id
     * @return user by id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    /**
     * Find all users
     *
     * @return user list
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    /**
     * Persist user
     *
     * @param userDTO user
     * @return persisted user
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserDTO> insertUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.insert(userDTO), HttpStatus.OK);

    }

    /**
     * Update user
     *
     * @param userDTO user
     * @return updated user
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") Integer id, @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.update(id, userDTO), HttpStatus.OK);
    }

    /**
     * Delete user
     *
     * @param id id
     * @return message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(@PathVariable("id") Integer id) {
        final Long deleted = userService.delete(id);
        if (deleted != null && deleted.intValue() == 1)
            return new ResponseEntity<>(messageSource.getMessage("message.user.deleted", null, LocaleContextHolder.getLocale()), HttpStatus.NO_CONTENT);
        else {
            return new ResponseEntity<>(messageSource.getMessage("error.no.user.found", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
        }
    }
}
