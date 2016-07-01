package com.visiontech.yummysmile.repository.api.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class to handle the response of User Information.
 *
 * @author hector.torres
 */
public class UserDTO implements Serializable {

    @SerializedName("objectId")
    private String id;
    private String name;
    private String lastName;
    private String email;
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private String password;
    private String token;
    private String socialNetworkType;
    private String socialNetworkUserId;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSocialNetworkType() {
        return socialNetworkType;
    }

    public void setSocialNetworkType(String socialNetwork) {
        this.socialNetworkType = socialNetwork;
    }

    public String getSocialNetworkUserId() {
        return socialNetworkUserId;
    }

    public void setSocialNetworkUserId(String socialNetworkUserId) {
        this.socialNetworkUserId = socialNetworkUserId;
    }
}
