package com.vedant.notification_service.constants;

import org.springframework.stereotype.Component;

@Component
public class ElasticsearchConstants {

    public static final String INDEX_NAME = "search-sms_requests";
    public static final String SERVER_ADDRESS = "localhost:9200";
    public static final String HOST_NAME = "localhost";
    public static final int PORT_NUMBER = 9200;
    public static final String SCHEME = "https";
    public static final String USER = "elastic";
    public static final String USER_PASSWORD = "KVl2apBG4_m*PvXn6vXm";
    public static final String CERTIFICATE_FINGERPRINT = "a7bb2521e7cb21fbd86e029ea37f783c24baf64e807a6ceef70dd210b8d135ae";


    public static final int PAGE_SIZE = 10;

    private ElasticsearchConstants(){}
}
