package com.robocontacts.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by ekaterina on 10.12.2016.
 */

@Entity
public class MatchedContacts extends BasicEntity implements Serializable {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Integer vkId;

    @Column(nullable = false)
    private String googleId;

    @Column(nullable = false)
    private String vkName;

    @Column(nullable = false)
    private String googleName;

    @Column(nullable = false)
    private String vkPhoto;

    @Column(nullable = false)
    private String googlePhoto;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getVkId() {
        return vkId;
    }

    public void setVkId(Integer vkId) {
        this.vkId = vkId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getVkName() {
        return vkName;
    }

    public void setVkName(String vkName) {
        this.vkName = vkName;
    }

    public String getGoogleName() {
        return googleName;
    }

    public void setGoogleName(String googleName) {
        this.googleName = googleName;
    }

    public String getVkPhoto() {
        return vkPhoto;
    }

    public void setVkPhoto(String vkPhoto) {
        this.vkPhoto = vkPhoto;
    }

    public String getGooglePhoto() {
        return googlePhoto;
    }

    public void setGooglePhoto(String googlePhoto) {
        this.googlePhoto = googlePhoto;
    }
}
