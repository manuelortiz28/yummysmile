package com.visiontech.yummysmile.util;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.visiontech.yummysmile.repository.api.response.ErrorResponse;
import com.visiontech.yummysmile.repository.api.response.ErrorResponseItem;
import com.visiontech.yummysmile.repository.api.response.HttpResponseCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.HttpException;

/**
 * @author manuel.ortiz
 *
 * Util class for handle errors related stuff
 *
 */
public final class ExceptionsUtil {
    private ExceptionsUtil() {
    }

    /**
     * Tries to extract the error response from retrofit exception,
     * or returns an generic exception if it is not a retrofit exception
     *
     * @param exception Exception to be identified, if it is not known, returns a generic exception
     * @return The ErrorResponse object with the related information
     */
    public static ErrorResponse getSpecificException(Throwable exception) {
        if (exception instanceof HttpException) {
            HttpException retrofitException = (HttpException) exception;

            if (HttpResponseCode.UNKNOWN != HttpResponseCode.fromValue(retrofitException.code())) {


                String errorBody = null;
                try {
                    errorBody = retrofitException.response().errorBody().string();
                    return new Gson().fromJson(errorBody, ErrorResponse.class);
                } catch (IOException e) {
                    return createGenericErrorResponse(exception);
                } catch (JsonParseException e) {
                    return createGenericErrorResponse(exception);
                }
            }
        }

        return createGenericErrorResponse(exception);
    }

    /**
     * Creates a generic error response, setting the error message from the exception
     * @param exception Exception where the error message is gonna be extracted
     * @return A generic error response, containing the exception error message
     */
    public static ErrorResponse createGenericErrorResponse(Throwable exception) {
        ErrorResponseItem errorResponseItem =
                new ErrorResponseItem(ErrorResponseItem.ReasonType.UNKNOWN, exception.getMessage());

        List<ErrorResponseItem> errorResponseItemList = new ArrayList<>();
        errorResponseItemList.add(errorResponseItem);

        return new ErrorResponse(HttpResponseCode.UNKNOWN, exception.getMessage(), errorResponseItemList);
    }
}
