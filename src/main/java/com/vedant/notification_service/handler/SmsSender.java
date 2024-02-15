package com.vedant.notification_service.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vedant.notification_service.constants.SmsSenderConstants;
import com.vedant.notification_service.exception.InternalServerException;
import com.vedant.notification_service.model.entity.SmsRequest;
import com.vedant.notification_service.model.entity.smsSender.requestBody.Channel;
import com.vedant.notification_service.model.entity.smsSender.requestBody.Destination;
import com.vedant.notification_service.model.entity.smsSender.requestBody.Message;
import com.vedant.notification_service.model.entity.smsSender.response.ResponseItem;
import com.vedant.notification_service.model.entity.smsSender.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SmsSender {
    @Autowired
    RestTemplate restTemplate;

    public Message createMessage(SmsRequest smsRequest){
        try{
            // Instantiate a Message object
            Message message = new Message();
            message.setDeliverychannel(SmsSenderConstants.DELIVERY_CHANNEL);

            // Instantiate a Channel object
            Channel channel = new Channel();
            channel.setText(smsRequest.getMessage());

            // Put the Channel object into a map
            Map<String, Channel> channels = new HashMap<>();
            channels.put(SmsSenderConstants.DELIVERY_CHANNEL, channel);
            message.setChannels(channels);

            // Instantiate a Destination object
            Destination destination = new Destination();

            // Set msisdn
            List<String> msisdnList = new ArrayList<>();
            msisdnList.add(smsRequest.getPhone_number());
            destination.setMsisdn(msisdnList);

            // Set correlationid
            destination.setCorrelationid(String.valueOf(smsRequest.getId()));

            // Put the Destination object into a list
            List<Destination> destinations = new ArrayList<>();
            destinations.add(destination);
            message.setDestination(destinations);

            System.out.println(message);
            return message;
        } catch (Exception ex){
            System.out.println("Exception creating message request body.");
            System.out.println(ex);
            throw new RuntimeException("Unable to create Message request body!");
        }
    }

    public ResponseItem sendSms(SmsRequest smsRequest){
        try{
            //creating req body
//            Message message = createMessage(smsRequest);
//            ArrayList<Message> reqBody = new ArrayList<>();
//            reqBody.add(message);
//
//            //converting to json object
//            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//            String jsonReqBody = ow.writeValueAsString(reqBody);
//
//            //setting up headers
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.set(SmsSenderConstants.KEY_NAME, SmsSenderConstants.KEY_PHRASE);
//
//            HttpEntity<String> request = new HttpEntity<String>(jsonReqBody, headers);
//
//            ResponseWrapper responseWrapper = restTemplate.postForObject(SmsSenderConstants.POST_URL, request, ResponseWrapper.class);
//
//            assert responseWrapper != null;
//
//            ResponseItem response = responseWrapper.getResponse().get(0);

            ResponseItem response = new ResponseItem("1001","abcd","Successful","id");
            System.out.println(response);
            return response;
        } catch (Exception e){
            System.out.println("Exception caught: " + e);
            System.out.println("Caused by: " + e.getCause());
            throw new InternalServerException("Unable to send sms!");
        }
    }
}
