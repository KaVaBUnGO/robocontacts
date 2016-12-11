package com.robocontacts.web;

import com.robocontacts.domain.*;
import com.robocontacts.repository.MatchedContactsRepository;
import com.robocontacts.service.GoogleService;
import com.robocontacts.service.MatchedContactsService;
import com.robocontacts.service.UserService;
import com.robocontacts.service.VkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Objects;

/**
 * Created by ekaterina on 25.10.2016.
 */

@Controller
public class ProfileController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final VkService vkService;
    private final GoogleService googleService;
    private final MatchedContactsService matchedContactsService;
    private final MatchedContactsRepository matchedContactsRepository;

    private List<MatchedContacts> matchedContactses;
    private List<FriendsInfoVk> vkFriends;
    private List<FriendsInfoGoogle> googleFriends;

    @Autowired
    public ProfileController(VkService vkService, UserService userService, GoogleService googleService, MatchedContactsService matchedContactsService, MatchedContactsRepository matchedContactsRepository) {
        super(userService);
        this.vkService = vkService;
        this.googleService = googleService;
        this.matchedContactsService = matchedContactsService;
        this.matchedContactsRepository = matchedContactsRepository;
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

        boolean googleConnected = user.getSocialPlatforms().stream().filter(connectedPlatform -> connectedPlatform.getSocialPlatform() == SocialPlatform.GOOGLE).count() > 0;

        List<MatchedContacts> matchedContactses = matchedContactsRepository.findAll();
        model.addAttribute("matchedContactses", matchedContactses);
        model.addAttribute("googleConnected", googleConnected);
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

    @RequestMapping(value = "/profile/select", method = RequestMethod.POST, params = {"selectContacts"})
    public String selectContacts(Model model)
    {
        if (vkFriends == null) {
            vkFriends = vkService.getFriendsInfo(true, null);
        }
        if (googleFriends == null) {
            googleFriends  = googleService.getFriendsInfoGoogle();
        }
        model.addAttribute("friendsInfoVk", vkFriends);
        model.addAttribute("friendsInfoGoogles", googleFriends);
        return getProfilePage(model);
    }


    @RequestMapping(value = "/profile/save", method = RequestMethod.POST, params = {"saveMatch"})
    public String saveMatch(@ModelAttribute("vkId") String vkId, @ModelAttribute("googleId") String googleId, Model model){


        FriendsInfoVk vkFriendsInfo = vkFriends.stream().filter(friendsInfoVk -> Objects.equals(friendsInfoVk.getUserId(), Integer.valueOf(vkId))).findFirst().get();
        FriendsInfoGoogle googleFriendsInfo = googleFriends.stream().filter(friendsInfoGoogle -> friendsInfoGoogle.getUserId().equals(googleId)).findAny().get();

        matchedContactsService.save(vkFriendsInfo, googleFriendsInfo);

        model.addAttribute("friendsInfoVk", vkFriends);
        model.addAttribute("friendsInfoGoogles", googleFriends);
        return getProfilePage(model);
    }


}
