package com.pentalog.bookstore.controllers;

import com.pentalog.bookstore.dto.RoleDTO;
import com.pentalog.bookstore.dto.RoleMapper;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Role;
import com.pentalog.bookstore.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private RoleMapper roleMapper;

    /**
     * Find role by name
     * @param name name
     * @return roles by name
     */
    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public ResponseEntity<Collection<RoleDTO>> findByName(@RequestParam("searchBy") String name) {
        return new ResponseEntity<>(roleMapper.toRoleDTOs(roleService.findByRoleName(name)), HttpStatus.OK);
    }

    /**
     * Find roles
     * @return all roles
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Collection<RoleDTO>> getAllRoles() {
        return new ResponseEntity<>(roleMapper.toRoleDTOs(roleService.findAll()), HttpStatus.OK);
    }

    /**
     * Insert role
     * @param roleDTO role
     * @return persisted role
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<RoleDTO> insertRole(@RequestBody RoleDTO roleDTO) {
        final Role role = roleMapper.toRole(roleDTO);
        return new ResponseEntity<>(roleMapper.toRoleDTO(roleService.insert(role)), HttpStatus.OK);
    }

    /**
     * Update role
     * @param roleDTO role
     * @return updated role
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public ResponseEntity<RoleDTO> updateRole(@PathVariable("id") Integer id, @RequestBody RoleDTO roleDTO) {
        final Role role = roleMapper.toRole(roleDTO);
        return new ResponseEntity<>(roleMapper.toRoleDTO(roleService.update(id, role)), HttpStatus.OK);
    }

    /**
     * Delete role by id
     * @param id id
     * @return status message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteRole(@PathVariable("id") Integer id) {
        final Long deleted = roleService.delete(id);
        if (deleted != null && deleted.intValue() == 1)
            return new ResponseEntity<>("Role successfully deleted", HttpStatus.NO_CONTENT);
        else
            throw new BookstoreException("Role not found!");
    }
}
