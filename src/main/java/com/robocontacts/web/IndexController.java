package com.robocontacts.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ekaterina on 12.10.2016.
 */

@Controller
public class IndexController {
    private static final Logger LOGGER    = LoggerFactory.getLogger( IndexController.class );
    private static final String PATH_ROOT = "/";
    @RequestMapping( PATH_ROOT )
    public String getHomePage( Model model ) {
        LOGGER.debug( "Getting home page" );
        return "index";
    }
}
