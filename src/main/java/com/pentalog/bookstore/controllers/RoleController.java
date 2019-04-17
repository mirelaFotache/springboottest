package com.pentalog.bookstore.controllers;

import com.pentalog.bookstore.dto.RoleDTO;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Resource
    private RoleService roleService;
    @Autowired
    private MessageSource messageSource;

    /**
     * Find role by name
     *
     * @param name name
     * @return roles by name
     */
    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public ResponseEntity<Collection<RoleDTO>> findByName(@RequestParam("searchBy") String name) {
        return new ResponseEntity<>(roleService.findByRoleName(name), HttpStatus.OK);
    }

    /**
     * Find roles one user have based on user id
     * @param userId user id
     * @return roles
     */
    @RequestMapping(value = "/{user_id}/roles", method = RequestMethod.GET)
    public ResponseEntity<Collection<RoleDTO>> getUserRoles(@PathVariable("user_id") Integer userId) {
        return new ResponseEntity<>(roleService.findRolesByUserId(userId), HttpStatus.OK);
    }


    /**
     * Find roles
     *
     * @return all roles
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<RoleDTO>> getAllRoles() {
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }

    /**
     * Insert role
     *
     * @param roleDTO role
     * @return persisted role
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RoleDTO> insertRole(@RequestBody RoleDTO roleDTO) {
        return new ResponseEntity<>(roleService.insert(roleDTO), HttpStatus.OK);
    }

    /**
     * Update role
     *
     * @param roleDTO role
     * @return updated role
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<RoleDTO> updateRole(@PathVariable("id") Integer id, @RequestBody RoleDTO roleDTO) {
        return new ResponseEntity<>(roleService.update(id, roleDTO), HttpStatus.OK);
    }

    /**
     * Delete role by id
     *
     * @param id id
     * @return status message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteRole(@PathVariable("id") Integer id) {
        final Long deleted = roleService.delete(id);
        if (deleted != null && deleted.intValue() == 1)
            return new ResponseEntity<>(messageSource.getMessage("message.role.deleted", null, LocaleContextHolder.getLocale()), HttpStatus.NO_CONTENT);
        else
            throw new BookstoreException(messageSource.getMessage("error.no.role.found", null, LocaleContextHolder.getLocale()));
    }
}
