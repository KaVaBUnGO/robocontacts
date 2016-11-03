package com.robocontacts.web;

import com.robocontacts.service.VkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by ekaterina on 25.10.2016.
 */
@Controller
public class ProfileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    protected static final String PATH_ROOT = "/profile";

    @Autowired
    private VkService vkService;

    @RequestMapping(PATH_ROOT)
    public String getProfilePage(Model model) {
        LOGGER.debug("Getting home page");
        return "profile";
    }

    @RequestMapping(value = PATH_ROOT, method = RequestMethod.POST, params = {"connectVk"})
    public String connectVk(Model model) {
        return "redirect:" + vkService.auth();
    }

    @RequestMapping(value = PATH_ROOT, method = RequestMethod.POST, params = {"connectGoogle"})
    public String build(Model model) {

        return "/profile";
    }

}
