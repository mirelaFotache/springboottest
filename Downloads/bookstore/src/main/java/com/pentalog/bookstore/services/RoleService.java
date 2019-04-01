package com.pentalog.bookstore.services;

import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.repositories.RoleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pentalog.bookstore.persistence.entities.Role;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class RoleService {

    @Resource
    private RoleJpaRepository roleJpaRepository;

    /**
     * Find role by name
     * @param roleName role name
     * @return role by name
     */
    public Collection<Role> findByRoleName(String roleName){
        return roleJpaRepository.findByName(roleName.toLowerCase());
    }

    /**
     * Find roles
     * @return roles
     */
    public Collection<Role> findAll() {
        return roleJpaRepository.findAll();
    }

    /**
     * Insert role
     * @param role role
     * @return inserted role
     */
    public Role insert(Role role) {
        return roleJpaRepository.save(role);
    }

    /**
     * Update role
     * @param role roel
     * @return updated role
     */
    public Role update(Integer id, Role role) {
        Role savedRole = null;
        Role persistedRole = roleJpaRepository.findById(id).orElse(null);

        if(persistedRole!=null && role!=null) {
            persistedRole.setName(role.getName());

            savedRole = roleJpaRepository.save(persistedRole);
        }else{
            throw new BookstoreException("Role not found!");
        }
        return savedRole;
    }

    /**
     * Delete role by id
     * @param id id
     * @return 0 when user not removed and 1 if user removed successfully
     */
    public Long delete(Integer id) {
        return roleJpaRepository.removeById(id);
    }
}
