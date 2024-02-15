package com.vedant.notification_service.helper;

import com.vedant.notification_service.model.requestBody.GetSmsBetweenDate;
import org.jose4j.http.Get;
import org.springframework.stereotype.Component;

@Component
public class ElasticsearchTestHelper {
    public GetSmsBetweenDate createGetSmsBetweenDateBody(){
        String phoneNumber = "+918085836022";
        String startDate = "2024-01-03 22:21:04";
        String endDate = "2024-01-03 22:21:04";
        int pageNumber = 0;
        return new GetSmsBetweenDate(phoneNumber, startDate, endDate, pageNumber);
    }
}
