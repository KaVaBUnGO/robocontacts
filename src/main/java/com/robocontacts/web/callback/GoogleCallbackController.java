package com.robocontacts.web.callback;

import com.robocontacts.service.GoogleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ekaterina on 15.11.2016.
 */
@Controller
public class GoogleCallbackController {
    private final GoogleService googleService;

    public GoogleCallbackController(GoogleService googleService) {
        this.googleService = googleService;
    }

    @RequestMapping("/oauth2callback")
    public String callback(HttpServletRequest httpServletRequest){
        googleService.connect(httpServletRequest.getParameter("code"));

        return "redirect:/profile";
    }

}
