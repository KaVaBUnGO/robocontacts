package com.robocontacts.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ekaterina on 07.12.2016.
 */

@Entity
@Table(name = "friends_info")
public class FriendsInfo extends BasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name = "big_photo_url")
    private String bigPhotoUrl;

    @Column(name = "medium_photo_url")
    private String mediumPhotoUrl;

    @Column(name = "small_photo_url")
    private String smallPhotoUrl;

    @Column(name = "phone_number")
    private String phoneNumber;

    public FriendsInfo() {
    }

    public String getBigPhotoUrl() {
        return bigPhotoUrl;
    }

    public void setBigPhotoUrl(String bigPhotoUrl) {
        this.bigPhotoUrl = bigPhotoUrl;
    }

    public String getMediumPhotoUrl() {
        return mediumPhotoUrl;
    }

    public void setMediumPhotoUrl(String mediumPhotoUrl) {
        this.mediumPhotoUrl = mediumPhotoUrl;
    }

    public String getSmallPhotoUrl() {
        return smallPhotoUrl;
    }

    public void setSmallPhotoUrl(String smallPhotoUrl) {
        this.smallPhotoUrl = smallPhotoUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
