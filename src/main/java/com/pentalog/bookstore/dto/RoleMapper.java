package com.pentalog.bookstore.dto;

import com.pentalog.bookstore.persistence.entities.Role;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface RoleMapper {

    RoleDTO toRoleDTO(Role role);

    Collection<RoleDTO> toRoleDTOs(Collection<Role> roles);

    Role toRole(RoleDTO roleDTO);

    List<Role> toRoles(List<RoleDTO> rolesDTO);

}
