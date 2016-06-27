package com.visiontech.yummysmile.util;

import com.visiontech.yummysmile.BuildConfig;

/**
 * Class to contains the repetitive constants in the project.
 * @author hector.torres
 */
public final class Constants {
    public static final String HOST = BuildConfig.RS_API_URL + BuildConfig.RS_API_APP_NAME;
    public static final String API = "api/";
    public static final String API_VERSION = "v1/";

    public static final String TOKEN_FIELD = "TOKEN";
    public static final String USER_FIELD = "userID";
    public static final String MEALS = "meals";
    public static final String API_MULTIPART_DATA = "multipart/form-data";

    private Constants() {
    }
}
