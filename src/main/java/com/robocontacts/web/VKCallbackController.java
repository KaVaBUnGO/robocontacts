package com.robocontacts.web;

import com.robocontacts.domain.CurrentUser;
import com.robocontacts.domain.SocialPlatform;
import com.robocontacts.repository.SocialPlatformRepository;
import com.robocontacts.service.VkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ekaterina on 30.10.2016.
 */

@Controller
public class VKCallbackController {


    private final VkService vkService;
    private final SocialPlatformRepository socialPlatformRepository;

    @Autowired
    public VKCallbackController(SocialPlatformRepository socialPlatformRepository, VkService vkService) {
        this.socialPlatformRepository = socialPlatformRepository;
        this.vkService = vkService;
    }

    @RequestMapping("callback")
    public String callback(HttpServletRequest httpServletRequest){
        vkService.connect(httpServletRequest.getParameter("code"));
        SocialPlatform socialPlatform = new SocialPlatform();
        socialPlatform.setAccessToken(httpServletRequest.getParameter("code"));
        CurrentUser currentUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        socialPlatform.setUser((currentUser.getUser()));
        socialPlatform.setName("Vk");
        socialPlatformRepository.save(socialPlatform);
        return "redirect:/profile";
    }

}
