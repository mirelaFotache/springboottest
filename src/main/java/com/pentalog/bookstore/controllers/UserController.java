package com.pentalog.bookstore.controllers;

import com.pentalog.bookstore.dto.RoleDTO;
import com.pentalog.bookstore.dto.RoleMapper;
import com.pentalog.bookstore.dto.UserDTO;
import com.pentalog.bookstore.dto.UserMapper;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.User;
import com.pentalog.bookstore.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper rolesMapper;
    //private Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * Find user by user name
     * @param userName user name
     * @return users
     */
    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public ResponseEntity<Collection<UserDTO>> findByUserName(@RequestParam("searchBy") String userName) {
        return new ResponseEntity<>(userMapper.toUserDTOs(userService.findByUserName(userName)), HttpStatus.OK);
    }

    /**
     * Get user by id
     * @param id id
     * @return user by id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(userMapper.toUserDTO(userService.findById(id)), HttpStatus.OK);
    }

    @RequestMapping(value = "/{user_id}/roles/", method = RequestMethod.GET)
    public ResponseEntity<Collection<RoleDTO>> getUserRoles(@PathVariable("user_id") Integer userId) {
        return new ResponseEntity<>(rolesMapper.toRoleDTOs(userService.findRolesByUserId(userId)), HttpStatus.OK);
    }

    /**
     * Find all users
     * @return user list
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Collection<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userMapper.toUserDTOs(userService.findAll()), HttpStatus.OK);
    }

    /**
     * Persist user
     * @param userDTO user
     * @return persisted user
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> insertUser(@RequestBody UserDTO userDTO) {
        final User user = userMapper.toUser(userDTO);
        user.setUserRoles(rolesMapper.toRoles(userDTO.getUserRoles()));
        return new ResponseEntity<>(userMapper.toUserDTO(userService.insert(user)), HttpStatus.OK);

    }

    /**
     * Update user
     * @param userDTO user
     * @return updated user
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") Integer id, @RequestBody UserDTO userDTO) {
        final User user = userMapper.toUser(userDTO);
        user.setUserRoles(rolesMapper.toRoles(userDTO.getUserRoles()));
        return new ResponseEntity<>(userMapper.toUserDTO(userService.update(id, user)), HttpStatus.OK);
    }

    /**
     * Delete user
     * @param id id
     * @return message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(@PathVariable("id") Integer id) {
        final Long deleted = userService.delete(id);
        if (deleted != null && deleted.intValue() == 1)
            return new ResponseEntity<>("User successfully deleted!", HttpStatus.NO_CONTENT);
        else {
            throw new BookstoreException("User not found!");
        }
    }
}
