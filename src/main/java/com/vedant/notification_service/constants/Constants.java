package com.vedant.notification_service.constants;

public class Constants {
    public static final String REQUEST_LOG = "Method : {}, Request : {}";
    public static final String RESPONSE_LOG = "Method : {}, Response : {}";
    public static final String BAD_REQUEST_LOG =
            "BadRequestException occurred in Method : {}, Request : {}, Stacktrace : {}";
    public static final String EXECUTION_EXCEPTION_LOG =
            "Execution Server Error in Method : {}, Request : {}, Stacktrace : {}";
    public static final String INTERNAL_SERVER_LOG =
            "Internal Server Error in Method : {}, Request : {}, Stacktrace : {}";
    public static final String EXCEPTION_LOG =
            "Exception occurred in Method : {}, Request : {}, Stacktrace : {}";
}
