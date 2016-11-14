package com.robocontacts.dto;

import java.io.Serializable;

/**
 * @author Artur Chernov
 */
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;
    private String userPhoto;
    private String userEmail;

    public UserDTO() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
