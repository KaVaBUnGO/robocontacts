package com.robocontacts;

import com.robocontacts.domain.CurrentUser;
import com.robocontacts.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Artur Chernov
 */

public class SecurityHelper {

    public static User getCurrentUser(){
        CurrentUser currentUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return currentUser.getUser();
    }
}
