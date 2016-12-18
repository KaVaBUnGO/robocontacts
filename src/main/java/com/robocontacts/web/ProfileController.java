package com.robocontacts.web;

import com.robocontacts.domain.ConnectedPlatform;
import com.robocontacts.domain.SocialPlatform;
import com.robocontacts.service.ConnectedPlatformService;
import com.robocontacts.service.GoogleService;
import com.robocontacts.service.UserService;
import com.robocontacts.service.VkService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by ekaterina on 25.10.2016.
 */

@Controller
@RequestMapping("/profile")
public class ProfileController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    private final VkService vkService;
    private final GoogleService googleService;
    private final ConnectedPlatformService connectedPlatformService;

    private Map<SocialPlatform, ConnectedPlatform> platforms = new HashMap<>();

    @Autowired
    public ProfileController(VkService vkService, UserService userService, GoogleService googleService, ConnectedPlatformService connectedPlatformService) {
        super(userService);
        this.vkService = vkService;
        this.googleService = googleService;
        this.connectedPlatformService = connectedPlatformService;
    }

    @PostConstruct
    public void init() {
        Arrays.asList(SocialPlatform.values()).stream().forEach(platform -> platforms.put(platform, null));
    }

    @ModelAttribute("platforms")
    public Map<SocialPlatform, ConnectedPlatform> getPlatforms() {
        if (getCurrentUser() != null) {
            Arrays.asList(SocialPlatform.values()).stream().forEach(platform -> platforms.put(platform, getCurrentUser()
                    .getSocialPlatforms().stream().filter(connectedPlatform -> connectedPlatform.getSocialPlatform().equals(platform)).findFirst().orElse(null)));
        }
        return platforms;
    }

    @RequestMapping
    public String getProfilePage(Model model) {
        model.addAttribute("platformsConnected", getCurrentUser().getSocialPlatforms().stream().count()==2);
        return "profile";
    }

    @RequestMapping(value = "/connectPlatform", method = RequestMethod.POST)
    public ResponseEntity<String> connectPlatform(@RequestParam("platform") SocialPlatform platform) {
        if (platform.equals(SocialPlatform.GOOGLE)) {
            return new ResponseEntity<>(googleService.getOAuthUrl(), HttpStatus.OK);
        } else if (platform.equals(SocialPlatform.VK)) {
            return new ResponseEntity<>(vkService.getOAuthUrl(), HttpStatus.OK);
        }
        return new ResponseEntity<>(StringUtils.EMPTY, HttpStatus.OK);
    }

    @RequestMapping(value = "/deletePlatform", method = RequestMethod.POST)
    public ResponseEntity<String> deletePlatform(@RequestParam("platform") SocialPlatform platform) {
        connectedPlatformService.delete(getCurrentUser().getSocialPlatforms().stream().filter(connectedPlatform -> connectedPlatform.getSocialPlatform().equals(platform)).findFirst().get());
        return new ResponseEntity<>(StringUtils.EMPTY, HttpStatus.OK);
    }
}
