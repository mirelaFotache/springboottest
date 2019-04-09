package com.pentalog.bookstore.controllers;

import com.pentalog.bookstore.persistence.entities.User;
import com.pentalog.bookstore.utils.UserSupplier;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

   /* @Before
    public void init() {
        User user = UserSupplier.supplyUserForInsert();
        responseEntity =
                restTemplate.postForEntity("http://localhost:" + port + "/bookstore/users/", user, User.class);
    }*/

    @Test
    public void bFindByUserName(){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                "http://localhost:8888/bookstore/users/name?searchBy={name}", String.class,"mira");

        Assert.assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCodeValue());
    }

    @Test
    public void cFindByUserNameNotFound() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                "http://localhost:8888/bookstore/users/name?searchBy={name}", String.class,"aaa");

        Assert.assertEquals(HttpStatus.NOT_FOUND.value(),responseEntity.getStatusCodeValue());
    }

    @Test
    public void dGetUser() {
        ResponseEntity<User> responseEntity = restTemplate.getForEntity(
                "http://localhost:8888/bookstore/users/{id}", User.class,"1");

        User user = responseEntity.getBody();

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(user);
        Assert.assertEquals("mira", user.getUserName());
    }

    @Test
    public void eGetAllUsers() {
        final ResponseEntity<List<User>> responseEntity = restTemplate.exchange(
                "http://localhost:8888/bookstore/users/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>(){});
        List<User> users = responseEntity.getBody();

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(users);
        Assert.assertEquals(1,users.size());
    }

    @Test
    public void aInsertUser() {
        User userToBePersisted = UserSupplier.supplyUserForInsert().get();
        ResponseEntity<User> responseEntity =
                restTemplate.postForEntity("http://localhost:" + port + "/bookstore/users/", userToBePersisted, User.class);
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
                restTemplate.exchange("http://localhost:" + port + "/bookstore/users/1", HttpMethod.PUT, requestEntity, User.class);
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
                restTemplate.exchange("http://localhost:" + port + "/bookstore/users/1", HttpMethod.DELETE, requestEntity, User.class);

        Assert.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void hDeleteUserNotFound() {
        User userToBePersisted = UserSupplier.supplyUserForUpdate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> requestEntity = new HttpEntity<>(userToBePersisted, requestHeaders);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("http://localhost:" + port + "/bookstore/users/2", HttpMethod.DELETE, requestEntity, String.class);
        String s = responseEntity.getBody();
        Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Assert.assertEquals("User not found!", s);
    }

}