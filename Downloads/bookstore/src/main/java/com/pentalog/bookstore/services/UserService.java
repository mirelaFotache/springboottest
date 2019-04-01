package com.pentalog.bookstore.services;

import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Role;
import com.pentalog.bookstore.persistence.entities.User;
import com.pentalog.bookstore.persistence.repositories.RoleJpaRepository;
import com.pentalog.bookstore.persistence.repositories.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@EnableTransactionManagement
public class UserService {

    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private RoleJpaRepository roleJpaRepository;


    /**
     * Find user by userName
     * @param userName user name
     * @return users
     */
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public Collection<User> findByUserName(String userName) {
        return userJpaRepository.findByUserName(userName.toLowerCase());
    }

    /**
     * Find user by id
     * @param id id
     * @return user by id
     */
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public User findById(Integer id) {
        return userJpaRepository.findById(id).orElse(null);
    }

    /**
     * Find roles of user by id user
     * @param userId user id
     * @return roles
     */
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public Collection<Role> findRolesByUserId(Integer userId) {
        return userJpaRepository.findUserRoles(userId);
    }

    /**
     * Find all users
     * @return all users
     */
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public Collection<User> findAll() {
        return userJpaRepository.findAll();
    }
    /**
     * Save user
     * @param user user
     * @return saved user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public User insert(User user) {
        return userJpaRepository.save(user);
    }

    /**
     * Update user
     * @param user user
     * @return updated user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public User update(Integer id, User user) {
        User savedUser;
        User persistedUser = userJpaRepository.findById(id).orElse(null);
        if(persistedUser!=null && user!=null) {
            persistedUser.setFirstName(user.getFirstName());
            persistedUser.setLastName(user.getLastName());
            persistedUser.setUserName(user.getUserName());
            persistedUser.setPhoneNumber(user.getPhoneNumber());
            persistedUser.setEmail(user.getEmail());
            persistedUser.setAddress(user.getAddress());
            persistedUser.setCity(user.getCity());
            persistedUser.setPassword(user.getPassword());
            persistedUser.getUserRoles().clear();

            for(Role r : user.getUserRoles()){
                Role role = roleJpaRepository.findById(r.getId()).orElse(null);
                persistedUser.getUserRoles().add(role);
            }

            savedUser = userJpaRepository.save(persistedUser);
       }else{
            throw new BookstoreException("User not found!");
        }
        return savedUser;
    }

    /**
     * Delete user by id
     * @param id id
     * @return 0 when user not removed and 1 if user removed successfully
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Long delete(Integer id) {
            return userJpaRepository.removeById(id);
    }
}
