package com.robocontacts.web;

import com.robocontacts.domain.User;
import com.robocontacts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author Artur Chernov
 */
@Controller
public class AbstractController {

    protected UserService userService;

    @ModelAttribute("currentUser")
    public User getCurrentUser(){
        return userService.getCurrentUser();
    }

    @Autowired
    public AbstractController(UserService userService) {
        this.userService = userService;
    }
}
