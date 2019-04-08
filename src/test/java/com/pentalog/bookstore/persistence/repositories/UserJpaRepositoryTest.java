package com.pentalog.bookstore.persistence.repositories;

import com.pentalog.bookstore.persistence.entities.User;
import com.pentalog.bookstore.utils.UserSupplier;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userRepo;

    @Test
    public void testFindByUserNameUserFound() {
        User user = UserSupplier.supplyUserWithUserNameAndId();
        userRepo.save(user);
        Collection<User> users = userRepo.findByUserName("mira");
        Assert.assertEquals(1,users.size());
    }

    @Test
    public void testFindByUserNameUserNotFound() {
        User user = UserSupplier.supplyUserWithUserNameAndId();
        userRepo.save(user);
        Collection<User> users = userRepo.findByUserName("");

        Assert.assertEquals(users.size(),0);
    }

    @Test
    public void testInsertUser() {
        User user = UserSupplier.supplyUserForInsert();
        User persistedUser = userRepo.save(user);
        Assert.assertTrue(persistedUser != null && persistedUser.getId() != 0 && persistedUser.getUserName().equals("mira"));
    }

}
