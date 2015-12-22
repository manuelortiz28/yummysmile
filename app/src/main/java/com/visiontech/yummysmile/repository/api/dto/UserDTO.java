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
    @SerializedName("lastname")
    private String lastName;
    private String email;

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
}
