package com.pentalog.bookstore.dto;

import com.pentalog.bookstore.persistence.entities.Role;
import com.pentalog.bookstore.persistence.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UsersMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Convert userDTO to user
     *
     * @param userDTOOptional userDTO of type Optional
     * @return optional
     */
    public Optional<User> fromDTO(Optional<UserDTO> userDTOOptional) {

        UserDTO userDTO = userDTOOptional.orElse(null);
        if (userDTO != null) {
            User user = new User();
            user.setId(userDTO.getId());
            user.setUserName(userDTO.getUserName());
            //user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setPassword(userDTO.getPassword());
            user.setCity(userDTO.getCity());
            user.setAddress(userDTO.getAddress());
            user.setEmail(userDTO.getEmail());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setActive(userDTO.isActive());
            if (userDTO.getUserRoles() != null) {
                List<Role> roles = new ArrayList<>();
                for (RoleDTO roleDTO : userDTO.getUserRoles()) {
                    Role role = new Role();
                    role.setId(roleDTO.getId());
                    role.setName(roleDTO.getName());
                    roles.add(role);
                }
                user.setUserRoles(roles);
            }
            return Optional.of(user);
        } else
            return Optional.empty();
    }

    /**
     * Convert user to userDto
     *
     * @param userOptional user of type Optional
     * @return optional
     */
    public Optional<UserDTO> toDTO(Optional<User> userOptional) {
        User user = userOptional.orElse(null);
        if (user != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUserName(user.getUserName());
            userDTO.setPassword(user.getPassword());
            userDTO.setCity(user.getCity());
            userDTO.setAddress(user.getAddress());
            userDTO.setEmail(user.getEmail());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setActive(user.isActive());
            if (user.getUserRoles() != null && user.getUserRoles().size() > 0) {
                List<RoleDTO> roles = new ArrayList<>();
                for (Role role : user.getUserRoles()) {
                    if (role != null) {
                        RoleDTO roleDTO = new RoleDTO();
                        roleDTO.setId(role.getId());
                        roleDTO.setName(role.getName());
                        roles.add(roleDTO);
                    }
                }
                userDTO.setUserRoles(roles);
            }
            return Optional.of(userDTO);
        } else {
            return Optional.empty();
        }

    }

}
