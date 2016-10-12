package com.robocontacts.service;

import com.robocontacts.domain.User;
import com.robocontacts.domain.UserCreateForm;

import java.util.List;

/**
 * Created by ekaterina on 11.10.2016.
 */
public interface UserService {
    User getUserById(long id);
    User getUserByLogin(String login);
    List<User> getAllUsers();
    User create(UserCreateForm form);
    User save(User user);
    void delete(User user);
}
