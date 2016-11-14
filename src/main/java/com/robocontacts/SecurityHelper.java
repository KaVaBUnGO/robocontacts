package com.robocontacts;

import com.robocontacts.domain.CurrentUser;
import com.robocontacts.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Artur Chernov
 */

public class SecurityHelper {

    public static User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            CurrentUser currentUser = (CurrentUser) principal;
            return currentUser.getUser();
        }
        return null;
    }
}
