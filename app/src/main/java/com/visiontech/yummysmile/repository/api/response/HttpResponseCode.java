package com.visiontech.yummysmile.repository.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author manuel.ortiz
 *
 * Enum with different http response codes from the Rest API
 */
public enum HttpResponseCode {
    @SerializedName("0")
    UNKNOWN(0),
    @SerializedName("401")
    UNAUTHORIZED(401),
    @SerializedName("405")
    NOT_FOUND(405),
    @SerializedName("500")
    INTERNAL_SERVER_ERROR(500);

    private int value;

    HttpResponseCode(int errorCode) {
        this.value = errorCode;
    }

    /**
     * Transforms an int value into an enum with the desired errorCode
     * @param errorCode
     * @return
     */
    public static HttpResponseCode fromValue(int errorCode) {
        if (UNAUTHORIZED.getValue() == errorCode) {
            return UNAUTHORIZED;
        }

        return UNKNOWN;
    }

    /**
     * Obtains the value of this enum
     * @return The error code corresponding to the current Api Error Code
     */
    public int getValue() {
        return value;
    }
}
