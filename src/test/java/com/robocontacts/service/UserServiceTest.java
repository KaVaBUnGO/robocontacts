package com.robocontacts.service;

import com.robocontacts.RoboContactsApp;
import com.robocontacts.domain.Role;
import com.robocontacts.domain.User;
import com.robocontacts.domain.UserCreateForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

/**
 * Created by ekaterina on 21.12.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RoboContactsApp.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;


    @Test
    public void testGetUserById() throws Exception {
        User user = userService.getUserById(1L);
        assertThat(user.getRole(), is(Role.ROLE_ADMIN));
    }



    @Test
    public void testGetUserByName() throws Exception {
        User user = userService.getUserByLogin("user");
        assertThat(user.getId(), is(2L));
    }

    @Test
    public void testGetUserByName1() throws Exception {
        User user = userService.getUserByLogin("admin");
        assertThat(user.getId(), is(1L));
    }


    @Test
    public void testGetAllUsers() throws Exception {
        assertThat(userService.getAllUsers().size(), greaterThan(2));
    }

    @Test
    public void testCreate() throws Exception {
        UserCreateForm userCreateForm = new UserCreateForm();
        userCreateForm.setLogin("testUser");
        userCreateForm.setPassword("test");
        User testUser = userService.create(userCreateForm);
        assertThat(testUser.getLogin(), is("testUser"));
    }

    @Test
    public void testCreate2() throws Exception {
        UserCreateForm userCreateForm = new UserCreateForm();
        userCreateForm.setLogin("superuser");
        userCreateForm.setPassword("tg53di6");
        User testUser = userService.create(userCreateForm);
        assertThat(testUser.getLogin(), is("superuser"));
    }


    @Test
    public void testSave() throws Exception {
        User user = new User();
        user.setLogin("test");
        user.setPassword("test");
        user.setRole(Role.ROLE_USER);
        userService.save(user);
        User testUser = userService.getUserByLogin("test");
        assertThat(testUser.getRole(), is(Role.ROLE_USER));
        assertThat(testUser.getLogin(), is("test"));
    }



    @Test
    public void testDelete() throws Exception {
        User user = userService.getUserById(1L);
        assertNotNull(user);
        userService.delete(userService.getUserById(1L));
        assertNull(userService.getUserById(1L));
    }

    @Test
    public void testDelete1() throws Exception {
        User user = userService.getUserById(2L);
        assertNotNull(user);
        userService.delete(userService.getUserById(2L));
        assertNull(userService.getUserById(2L));
    }
}