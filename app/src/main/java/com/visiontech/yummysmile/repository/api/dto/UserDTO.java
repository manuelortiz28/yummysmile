package com.visiontech.yummysmile.repository.api.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class to handle the response of User Information.
 * @author hector.torres
 */
public class UserDTO implements Serializable {

    @SerializedName("objectId")
    String id;
    String name;
    @SerializedName("lastname")
    String lastName;
    String email;

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
}
