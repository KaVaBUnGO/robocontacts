package com.robocontacts.web;

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

    @Autowired
    private VkService vkService;


    @RequestMapping("callback")
    public String callback(HttpServletRequest httpServletRequest){
        vkService.connect(httpServletRequest.getParameter("code"));
        return "redirect:/profile";
    }

}
