package com.visiontech.yummysmile.models;

import android.text.TextUtils;

/**
 * @author manuel.ortiz
 *
 * User model, containing all the information related to a user
 *
 */
public class User {
    private String id;
    private String name;
    private String lastName;
    private String email;
    private String token;
    private String socialNetworkType;
    private String socialNetworkUserId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Get the full name of the user
     * @return String representing the user full name, concatenating name and lastName
     */
    public String getFullName() {
        return TextUtils.join(" ", new String[]{name, lastName});
    }

    public void setSocialNetworkType(String socialNetwork) {
        this.socialNetworkType = socialNetwork;
    }

    public void setSocialNetworkUserId(String socialNetworkUserId) {
        this.socialNetworkUserId = socialNetworkUserId;
    }

    public String getSocialNetworkType() {
        return socialNetworkType;
    }

    public String getSocialNetworkUserId() {
        return socialNetworkUserId;
    }
}
