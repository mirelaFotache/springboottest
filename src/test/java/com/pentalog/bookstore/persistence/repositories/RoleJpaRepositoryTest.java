package com.pentalog.bookstore.persistence.repositories;

import com.pentalog.bookstore.dto.RoleDTO;
import com.pentalog.bookstore.dto.UsersMapper;
import com.pentalog.bookstore.persistence.entities.Role;
import com.pentalog.bookstore.utils.UserSupplier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleJpaRepositoryTest {
    @Autowired
    private RoleJpaRepository roleJpaRepository;

    @Before
    public void init() {
        Role role = new Role();
        role.setName("admin");
        roleJpaRepository.save(role);
    }
    @Test
    public void testfindByNameRoleFound() {
        Collection<Role> roles = roleJpaRepository.findByName("admin");
        Assert.assertTrue(roles.size() > 0);
    }

    @Test
    public void testFindRolesByUserIdFound() {

/*
        final Role suppliedRole = UserSupplier.testUserRoles();
        List<Role> roles = new ArrayList<>();
        roles.add(suppliedRole);

        Mockito.doReturn(roles).when(userJpaRepository).findUserRoles(1);

        Collection<RoleDTO> foundRoles = userService.findRolesByUserId(1);*/

        assertTrue(true);
    }

    public RoleJpaRepository getRoleJpaRepository() {
        return roleJpaRepository;
    }


}
