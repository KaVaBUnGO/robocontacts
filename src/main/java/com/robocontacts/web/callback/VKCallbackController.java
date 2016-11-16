package com.robocontacts.web.callback;

import com.robocontacts.service.ConnectedPlatformService;
import com.robocontacts.service.VkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ekaterina on 30.10.2016.
 */

@Controller
public class VKCallbackController {

    private final VkService vkService;
    private final ConnectedPlatformService connectedPlatformService;

    @Autowired
    public VKCallbackController(ConnectedPlatformService connectedPlatformService, VkService vkService) {
        this.connectedPlatformService = connectedPlatformService;
        this.vkService = vkService;
    }

    @RequestMapping("/callback")
    public String callback(HttpServletRequest httpServletRequest){
        vkService.connect(httpServletRequest.getParameter("code"));
        return "redirect:/profile";
    }
}
