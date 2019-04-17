package com.pentalog.bookstore.controllers;

import com.pentalog.bookstore.persistence.entities.Role;
import com.pentalog.bookstore.persistence.entities.User;
import com.pentalog.bookstore.persistence.repositories.UserJpaRepository;
import com.pentalog.bookstore.utils.UserSupplier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerIntegrationTest {

    private static boolean setUpIsDone = false;
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    private Logger logger = LoggerFactory.getLogger(UserControllerIntegrationTest.class);
    @Autowired
    private UserJpaRepository userRepo;

    @Before
    public void setUp() {
        if (setUpIsDone) {
            return;
        }
        // do the setup
        setUpIsDone = true;

        logger.info("executed only once, before the first test");
        User defaultUser = new User();
        defaultUser.setUserName("adminUser");
        defaultUser.setPassword("s3cret");
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setName("admin");
        roles.add(role);
        defaultUser.setUserRoles(roles);
        userRepo.save(defaultUser);
    }

    @Test
    public void bFindByUserName() {
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("adminUser", "s3cret").getForEntity(
                "http://localhost:8888/bookstore/users/name?searchBy={name}", String.class, "mira");

        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void cFindByUserNameNotFound() {
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("adminUser", "s3cret").getForEntity(
                "http://localhost:8888/bookstore/users/name?searchBy={name}", String.class, "abcd");

        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void dGetUser() {
        ResponseEntity<User> responseEntity = restTemplate.withBasicAuth("adminUser", "s3cret").getForEntity(
                "http://localhost:8888/bookstore/users/{id}", User.class, "3");

        User user = responseEntity.getBody();

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(user);
        Assert.assertEquals("mira", user.getUserName());
    }

    @Test
    public void eGetAllUsers() {
        final ResponseEntity<List<User>> responseEntity = restTemplate.withBasicAuth("adminUser", "s3cret").exchange(
                "http://localhost:8888/bookstore/users/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {
                });
        List<User> users = responseEntity.getBody();

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(users);
        Assert.assertTrue(users.size()>0);
    }

    @Test
    public void aInsertUser() {
        User userToBePersisted = UserSupplier.supplyUserForInsert().get();
        ResponseEntity<User> responseEntity =
                restTemplate.withBasicAuth("adminUser", "s3cret").postForEntity("http://localhost:" + port + "/bookstore/users/", userToBePersisted, User.class);
        User user = responseEntity.getBody();

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(user);
        Assert.assertEquals("mira", user.getUserName());
    }

    @Test
    public void fUpdateUser() {
        User userToBePersisted = UserSupplier.supplyUserForUpdate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> requestEntity = new HttpEntity<>(userToBePersisted, requestHeaders);
        ResponseEntity<User> responseEntity =
                restTemplate.withBasicAuth("adminUser", "s3cret").exchange("http://localhost:" + port + "/bookstore/users/3", HttpMethod.PUT, requestEntity, User.class);
        User user = responseEntity.getBody();

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(user);
        Assert.assertEquals("delia", user.getUserName());
    }

    @Test
    public void gDeleteUser() {
        User userToBePersisted = UserSupplier.supplyUserForUpdate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> requestEntity = new HttpEntity<>(userToBePersisted, requestHeaders);
        ResponseEntity<User> responseEntity =
                restTemplate.withBasicAuth("adminUser", "s3cret").exchange("http://localhost:" + port + "/bookstore/users/3", HttpMethod.DELETE, requestEntity, User.class);

        Assert.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void hDeleteUserNotFound() {
        User userToBePersisted = UserSupplier.supplyUserForUpdate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> requestEntity = new HttpEntity<>(userToBePersisted, requestHeaders);
        ResponseEntity<String> responseEntity =
                restTemplate.withBasicAuth("adminUser", "s3cret").exchange("http://localhost:" + port + "/bookstore/users/3", HttpMethod.DELETE, requestEntity, String.class);
        String s = responseEntity.getBody();
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Assert.assertEquals("No user found!", s);
    }

}