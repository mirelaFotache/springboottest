package com.pentalog.bookstore.dto;

import com.pentalog.bookstore.persistence.entities.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    /**
     * Convert role to roleDTO
     *
     * @param role role
     * @return roleDTO
     */
    public RoleDTO toDto(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        return roleDTO;
    }

    /**
     * Convert roleDTO to role
     * @param roleDTO roleDTO
     * @return role
     */
    public Role fromDto(RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());
        return role;
    }

}
