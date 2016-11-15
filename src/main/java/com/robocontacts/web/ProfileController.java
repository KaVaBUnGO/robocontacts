package com.robocontacts.web;

import com.robocontacts.domain.SocialPlatform;
import com.robocontacts.domain.User;
import com.robocontacts.service.GoogleService;
import com.robocontacts.service.UserService;
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
public class ProfileController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final VkService vkService;
    private final GoogleService googleService;

    @Autowired
    public ProfileController(VkService vkService, UserService userService, GoogleService googleService) {
        super(userService);
        this.vkService = vkService;
        this.googleService = googleService;
    }

    @RequestMapping("/profile")
    public String getProfilePage(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        boolean vkConnected = user.getSocialPlatforms().stream().filter(connectedPlatform -> connectedPlatform.getSocialPlatform() == SocialPlatform.VK).count() > 0;
        if (vkConnected) {
            model.addAttribute("vkPhotoUrl", user.getUserInfo().getBigPhotoUrl());
        }
        model.addAttribute("vkConnected", vkConnected);

        model.addAttribute("googleConnected", user.getSocialPlatforms().stream().filter(connectedPlatform -> connectedPlatform.getSocialPlatform() == SocialPlatform.GOOGLE).count() > 0);
        log.debug("Getting home page");
        return "profile";
    }

    /*
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String displayProfileVk(Model model){

        return "/vkPage";
    }
*/
    @RequestMapping(value = "/profile", method = RequestMethod.POST, params = {"connectVk"})
    public String connectVk(Model model) {
        return "redirect:" + vkService.getOAuthUrl();
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST, params = {"connectGoogle"})
    public String connectGoogle(Model model)
    {
        return "redirect:" + googleService.getOAuthUrl();
    }

}
