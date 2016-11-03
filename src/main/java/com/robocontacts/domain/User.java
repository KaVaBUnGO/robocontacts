package com.robocontacts.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekaterina on 15.09.2016.
 */

@Entity
public class User extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<ConnectedPlatform> socialPlatforms = new ArrayList<>();

    public List<ConnectedPlatform> getSocialPlatforms() {
        return socialPlatforms;
    }

    public void setSocialPlatforms(List<ConnectedPlatform> socialPlatforms) {
        this.socialPlatforms = socialPlatforms;
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
