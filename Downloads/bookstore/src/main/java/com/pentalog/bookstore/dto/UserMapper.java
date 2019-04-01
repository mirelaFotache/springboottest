package com.pentalog.bookstore.dto;

import com.pentalog.bookstore.persistence.entities.User;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface UserMapper {

    UserDTO toUserDTO(User user);

    Collection<UserDTO> toUserDTOs(Collection<User> users);

    User toUser(UserDTO userDTO);
}
