package com.robocontacts.service;

import com.robocontacts.domain.Role;
import com.robocontacts.domain.User;
import com.robocontacts.domain.UserCreateForm;
import com.robocontacts.repository.UserRepository;
import com.robocontacts.util.SecurityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekaterina on 11.10.2016.
 */

@Service
@Transactional
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(long id) {
        log.debug("Getting user ={}", id);
        return userRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public User getCurrentUser(){
        User user = SecurityHelper.getCurrentUser();
        if (user == null){
            return null;
        }
        return getUserById(user.getId());
    }

    public User getUserByLogin(String login) {
        log.debug("Getting user by name ={}", login);
        return userRepository.findByLogin(login);
    }

    public List<User> getAllUsers() {
        log.debug("Getting all users");
        List<User> users = new ArrayList<>();
        for (User user:userRepository.findAll()){
            users.add(user);
        }
        return users;
    }

    public User create(UserCreateForm form) {
        log.debug("Creating new user");
        User user = new User();
        user.setLogin(form.getLogin());
        user.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
        user.setRole(Role.ROLE_USER);
        return userRepository.save(user);
    }

    public User save(User user) {
        log.debug("Saving user");
        if (user.getId() == null){
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public void delete(User user) {
        log.debug("Deleting user");
        userRepository.delete(user);
    }
}
