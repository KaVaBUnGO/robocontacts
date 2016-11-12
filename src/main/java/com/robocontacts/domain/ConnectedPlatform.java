package com.robocontacts.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ekaterina on 27.10.2016.
 */
@Entity
@Table(name = "connected_platform")
public class ConnectedPlatform extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialPlatform socialPlatform;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "expires_in", nullable = false)
    private long expiresIn;

    @Column(name = "vk_id", nullable = false)
    private long vkId;

    public SocialPlatform getSocialPlatform() {
        return socialPlatform;
    }

    public void setSocialPlatform(SocialPlatform socialPlatform) {
        this.socialPlatform = socialPlatform;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public long getVkId() {
        return vkId;
    }

    public void setVkId(long vkId) {
        this.vkId = vkId;
    }
}
