package com.robocontacts.web;

import com.robocontacts.domain.FriendsInfoGoogle;
import com.robocontacts.domain.FriendsInfoVk;
import com.robocontacts.domain.MatchedContacts;
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
@RequestMapping("/contacts")
public class LinkContactsController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(LinkContactsController.class);

    private final VkService vkService;
    private final GoogleService googleService;
    private final MatchedContactsService matchedContactsService;
    private final MatchedContactsRepository matchedContactsRepository;

    private List<MatchedContacts> matchedContactses;

    private List<FriendsInfoVk> vkFriends;
    private List<FriendsInfoGoogle> googleFriends;

    @Autowired
    public LinkContactsController(VkService vkService, UserService userService, GoogleService googleService, MatchedContactsService matchedContactsService, MatchedContactsRepository matchedContactsRepository) {
        super(userService);
        this.vkService = vkService;
        this.googleService = googleService;
        this.matchedContactsService = matchedContactsService;
        this.matchedContactsRepository = matchedContactsRepository;
    }

    @RequestMapping
    public String getLinkContactsPage(Model model) {
        boolean platformsConnected = getCurrentUser().getSocialPlatforms().stream().count() == 2;
        model.addAttribute("platformsConnected", platformsConnected);
        if (platformsConnected) {
            fillContacts(model);
        }
        List<MatchedContacts> matchedContactses = matchedContactsRepository.findAll();
        model.addAttribute("matchedContactses", matchedContactses);
        return "linkContacts";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveMatch(@ModelAttribute("vkId") String vkId, @ModelAttribute("googleId") String googleId, Model model) {
        String newGoogleId = googleId.replace("@", "%40");
        FriendsInfoVk vkFriendsInfo = vkFriends.stream().filter(friendsInfoVk -> Objects.equals(friendsInfoVk.getUserId(), Integer.valueOf(vkId))).findFirst().get();
        FriendsInfoGoogle googleFriendsInfo = googleFriends.stream().filter(friendsInfoGoogle -> friendsInfoGoogle.getUserId().equals(googleId.replace("@", "%40"))).findAny().get();
        matchedContactsService.save(vkFriendsInfo, googleFriendsInfo);
        return getLinkContactsPage(model);
    }

    @RequestMapping(value = "/deleteLink", method = RequestMethod.POST)
    public String deleteMatch(@ModelAttribute("matchId") Long matchId, Model model) {

        matchedContactsRepository.delete(matchId);
        return getLinkContactsPage(model);
    }



    private void fillContacts(Model model) {
        if (vkFriends == null) {
            vkFriends = vkService.getFriendsInfo(true, null);
        }
        if (googleFriends == null) {
            googleFriends = googleService.getFriendsInfoGoogle();
        }
        model.addAttribute("vkFriends", vkFriends);
        model.addAttribute("googleFriends", googleFriends);
    }
}
