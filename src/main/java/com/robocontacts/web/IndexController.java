package com.robocontacts.web;

import com.robocontacts.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ekaterina on 12.10.2016.
 */
@Controller
public class IndexController extends AbstractController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    public IndexController(UserService userService) {
        super(userService);
    }

    @RequestMapping("/")
    public String getIndexPage(Model model) {
        LOGGER.debug("Getting home page");
        return "index";
    }
}
