package com.pentalog.bookstore.services;

import com.pentalog.bookstore.dto.RoleDTO;
import com.pentalog.bookstore.dto.RoleMapper;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Role;
import com.pentalog.bookstore.persistence.repositories.RoleJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RoleService {

    @Resource
    private RoleJpaRepository roleJpaRepository;
    @Autowired
    private RoleMapper roleMapper;
    /**
     * Find role by name
     *
     * @param roleName role name
     * @return role by name
     */
    public Collection<RoleDTO> findByRoleName(String roleName) {
        return roleJpaRepository.findByName(roleName.toLowerCase()).stream().map(roleMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Find roles of user by id user
     *
     * @param userId user id
     * @return roles
     */
    public Collection<RoleDTO> findRolesByUserId(Integer userId) {
        return roleJpaRepository.findUserRoles(userId).stream().map(roleMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Find roles
     *
     * @return roles
     */
    public Collection<RoleDTO> findAll() {
        return roleJpaRepository.findAll().stream().map(roleMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Insert role
     *
     * @param roleDTO roleDTO
     * @return inserted roleDTO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public RoleDTO insert(RoleDTO roleDTO) {
        return roleMapper.toDto(roleJpaRepository.save(roleMapper.fromDto(roleDTO)));
    }

    /**
     * Update role
     *
     * @param roleDTO roleDTO
     * @return updated roleDTO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public RoleDTO update(Integer id, RoleDTO roleDTO) {
        Role persistedRole = roleJpaRepository.findById(id).orElse(null);

        if (persistedRole != null && roleDTO != null) {
            persistedRole.setName(roleDTO.getName());

            return roleMapper.toDto(roleJpaRepository.save(persistedRole));
        } else {
            throw new BookstoreException("Role not found!");
        }
    }

    /**
     * Delete role by id
     *
     * @param id id
     * @return 0 when user not removed and 1 if user removed successfully
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Long delete(Integer id) {
        return roleJpaRepository.removeById(id);
    }
}
