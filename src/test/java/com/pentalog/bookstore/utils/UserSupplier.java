package com.pentalog.bookstore.utils;

import com.pentalog.bookstore.dto.RoleDTO;
import com.pentalog.bookstore.dto.UserDTO;
import com.pentalog.bookstore.persistence.entities.Role;
import com.pentalog.bookstore.persistence.entities.User;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

public class UserSupplier {

    public static User supplyUserWithUserNameAndId(){
        User user = new User();
        user.setId(1);
        user.setUserName("mira");
        return user;
    }

    public static Optional<User> supplyUserForInsert(){
        User user = new User();
        user.setId(1);
        user.setUserName("mira");
        return Optional.of(user);
    }

    public static UserDTO supplyUserDTOForInsert(){
        UserDTO user = new UserDTO();
        user.setId(1);
        user.setUserName("mira");
        return user;
    }
    public static User supplyUserForUpdate(){
        User user = new User();
        user.setId(1);
        user.setUserName("delia");

        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setId(1);
        role.setName("admin");
        roles.add(role);
        user.setUserRoles(roles);
        return user;
    }

    public static UserDTO supplyUserDTOForUpdate(){
        UserDTO user = new UserDTO();
        user.setId(1);
        user.setUserName("mira");

        List<RoleDTO> roles = new ArrayList<>();
        RoleDTO role = new RoleDTO();
        role.setId(1);
        role.setName("admin");
        roles.add(role);
        user.setUserRoles(roles);
        return user;
    }
}
