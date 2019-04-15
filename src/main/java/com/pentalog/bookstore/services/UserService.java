package com.pentalog.bookstore.services;

import com.pentalog.bookstore.dto.RoleDTO;
import com.pentalog.bookstore.dto.UserDTO;
import com.pentalog.bookstore.dto.UsersMapper;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Role;
import com.pentalog.bookstore.persistence.entities.User;
import com.pentalog.bookstore.persistence.repositories.RoleJpaRepository;
import com.pentalog.bookstore.persistence.repositories.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserService {

    @Autowired
    private MessageSource messageSource;
    private UserJpaRepository userJpaRepository;
    private UsersMapper userMapper;
    private RoleJpaRepository roleJpaRepository;


    public UserService(UserJpaRepository userJpaRepository, UsersMapper userMapper, RoleJpaRepository roleJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
        this.roleJpaRepository = roleJpaRepository;
    }

    /**
     * Find user by userName
     *
     * @param userName user name
     * @return users
     */
    public Collection<UserDTO> findByUserName(String userName) {
        return userJpaRepository.findByUserName(userName.toLowerCase()).stream()
                .map(user -> userMapper.toDTO(Optional.ofNullable(user)).orElse(null))
                .collect(Collectors.toList());
    }


    public Collection<UserDTO> findByUserNameAndPassword(String userName, String password) {
        return userJpaRepository.findByUserNameAndPass(userName, password).stream()
                .map(user -> userMapper.toDTO(Optional.ofNullable(user)).orElse(null))
                .collect(Collectors.toList());
    }

    /**
     * Find user by id
     *
     * @param id id
     * @return user by id
     */
    public UserDTO findById(Integer id) {
        return userMapper.toDTO(userJpaRepository.findById(id)).orElse(null);
    }


    /**
     * Find all users
     *
     * @return all users
     */
    public Collection<UserDTO> findAll() {
        return userJpaRepository.findAll().stream()
                .map(user -> userMapper.toDTO(Optional.ofNullable(user)).orElse(null))
                .collect(Collectors.toList());
    }

    /**
     * Save user
     *
     * @param userDTO userDTO
     * @return saved userDTO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDTO insert(UserDTO userDTO) {
        if (userDTO != null) {
            final Optional<User> userOptional = userMapper.fromDTO(Optional.of(userDTO));
            userOptional.ifPresent(user -> user.getUserRoles().clear());

            for (RoleDTO rDTO : userDTO.getUserRoles()) {
                Role role = roleJpaRepository.findById(rDTO.getId()).orElse(null);
                userOptional.ifPresent(user -> user.getUserRoles().add(role));
            }

            return userMapper.toDTO(Optional.ofNullable(userJpaRepository.save(userOptional.get()))).orElse(null);
        } else {
            throw new BookstoreException(messageSource.getMessage("error.no.user.found", null, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * Update user
     *
     * @param userDTO userDTO
     * @return updated user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDTO update(Integer id, UserDTO userDTO) {
        User persistedUser = userJpaRepository.findById(id).orElse(null);
        if (persistedUser != null && userDTO != null) {
            persistedUser.setFirstName(userDTO.getFirstName());
            persistedUser.setLastName(userDTO.getLastName());
            persistedUser.setUserName(userDTO.getUserName());
            persistedUser.setPhoneNumber(userDTO.getPhoneNumber());
            persistedUser.setEmail(userDTO.getEmail());
            persistedUser.setAddress(userDTO.getAddress());
            persistedUser.setCity(userDTO.getCity());
            persistedUser.setPassword(userDTO.getPassword());
            persistedUser.getUserRoles().clear();

            for (RoleDTO rDTO : userDTO.getUserRoles()) {
                Role role = roleJpaRepository.findById(rDTO.getId()).orElse(null);
                persistedUser.getUserRoles().add(role);
            }

            return userMapper.toDTO(Optional.ofNullable(userJpaRepository.save(persistedUser))).orElse(null);
        } else {
            throw new BookstoreException(messageSource.getMessage("error.no.user.found", null, LocaleContextHolder.getLocale()));
        }
    }

    /**
     * Delete user by id
     *
     * @param id id
     * @return 0 when user not removed and 1 if user removed successfully
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Long delete(Integer id) {
        return userJpaRepository.removeById(id);
    }


    UserJpaRepository getUserJpaRepository() {
        return userJpaRepository;
    }

    public RoleJpaRepository getRoleJpaRepository() {
        return roleJpaRepository;
    }
}
