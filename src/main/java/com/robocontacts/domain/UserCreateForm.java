package com.robocontacts.domain;

/**
 * Created by ekaterina on 11.10.2016.
 */
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

public class UserCreateForm {
    @NotEmpty
    private String login="";

    @NotEmpty
    private String password="";

    @NotEmpty
    private String passwordRepeated="";

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeated() {
        return passwordRepeated;
    }

    public void setPasswordRepeated(String passwordRepeated) {
        this.passwordRepeated = passwordRepeated;
    }

}
