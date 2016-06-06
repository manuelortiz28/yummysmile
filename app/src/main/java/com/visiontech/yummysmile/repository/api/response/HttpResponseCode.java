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
    @SerializedName("400")
    BAD_REQUEST(400),
    @SerializedName("401")
    UNAUTHORIZED(401),
    @SerializedName("404")
    NOT_FOUND(404),
    @SerializedName("405")
    METHOD_NOT_ALLOWED(405),
    @SerializedName("409")
    CONFLICT(409),
    @SerializedName("415")
    UNSUPPORTED_MEDIA_TYPE(415),
    @SerializedName("422")
    UNPROCESSABLE_ENTITY(422),
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
        for (HttpResponseCode currentCode : values()) {
            if (currentCode.value == errorCode) {
                return currentCode;
            }
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
