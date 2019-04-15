package com.pentalog.bookstore.services;

import com.pentalog.bookstore.dto.UserDTO;
import com.pentalog.bookstore.dto.UsersMapper;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Role;
import com.pentalog.bookstore.persistence.entities.User;
import com.pentalog.bookstore.persistence.repositories.RoleJpaRepository;
import com.pentalog.bookstore.persistence.repositories.UserJpaRepository;
import com.pentalog.bookstore.utils.RoleSupplier;
import com.pentalog.bookstore.utils.UserSupplier;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserJpaRepository.class)
public class UserServiceTest {

    private UserService userService = new UserService(Mockito.mock(UserJpaRepository.class), new UsersMapper(), Mockito.mock(RoleJpaRepository.class));
    private UserJpaRepository userJpaRepository = userService.getUserJpaRepository();
    private RoleJpaRepository roleJpaRepository = userService.getRoleJpaRepository();
    private List<User> users = new ArrayList<>();

    @Test
    public void testFindByUserNameFound() {
        final User suppliedUser = UserSupplier.supplyUserWithUserNameAndId();
        users.add(suppliedUser);
        Mockito.doReturn(suppliedUser).when(userJpaRepository).findByUserName("mira");

        UserDTO foundUser = userService.findByUserName("mira");


        assertEquals("mira", foundUser.getUserName());
        assertEquals(1, foundUser.getId());
    }

    @Test
    public void testFindByIdFound() {
        final User suppliedUser = UserSupplier.supplyUserWithUserNameAndId();
        Mockito.doReturn(Optional.of(suppliedUser)).when(userJpaRepository).findById(1);

        UserDTO foundUser = userService.findById(1);

        assertNotNull(foundUser);
        assertEquals("mira", foundUser.getUserName());
        assertEquals(1, foundUser.getId());
    }


    @Test
    public void findAll() {
        final User suppliedUser = UserSupplier.supplyUserWithUserNameAndId();
        users.add(suppliedUser);
        Mockito.doReturn(users).when(userJpaRepository).findAll();

        Collection<UserDTO> foundUsers = userService.findAll();
        List<UserDTO> users = new ArrayList<>(foundUsers);

        assertEquals(1, foundUsers.size());
        assertEquals("mira", users.get(0).getUserName());
        assertEquals(1, users.get(0).getId());

    }

    @Test
    public void insert() {
        User suppliedUser = new User();
        if (UserSupplier.supplyUserForInsert().isPresent())
            suppliedUser = UserSupplier.supplyUserForInsert().get();
        final UserDTO suppliedUserDto = UserSupplier.supplyUserDTOForInsert();

        Mockito.doReturn(suppliedUser).when(userJpaRepository).save(suppliedUser);
        UserDTO userDTO = userService.insert(suppliedUserDto);
        assertEquals(1, userDTO.getId());
        assertEquals("mira", userDTO.getUserName());
    }

    @Test
    public void update() {
        final User suppliedUser = UserSupplier.supplyUserForUpdate();
        final Role suppliedRole = RoleSupplier.supplyRoleForUpdate();
        final UserDTO suppliedUserDto = UserSupplier.supplyUserDTOForUpdate();
        Mockito.doReturn(Optional.of(suppliedUser)).when(userJpaRepository).findById(1);
        Mockito.doReturn(Optional.of(suppliedRole)).when(roleJpaRepository).findById(1);
        Mockito.doReturn(suppliedUser).when(userJpaRepository).save(suppliedUser);
        UserDTO userDTO = userService.update(suppliedUserDto.getId(), suppliedUserDto);
        assertEquals(1, userDTO.getId());
        assertEquals("mira", userDTO.getUserName());
    }

    @Test
    public void updateTestUserNotFound() {
        MessageSource messageSource = Mockito.mock(MessageSource.class);
        userService.setMessageSource(messageSource);
        Mockito.when(messageSource.getMessage("error.no.user.found", null, LocaleContextHolder.getLocale())).thenReturn("No user found!");
        Assert.assertThrows(BookstoreException.class, () ->
                userService.update(1, null));
    }

    @Test
    public void delete() {
        Long removedId = 1L;
        Mockito.doReturn(removedId).when(userJpaRepository).removeById(1);
        Long result = userService.delete(1);
        assertEquals(1, result.intValue());
    }

}