package com.vedant.notification_service.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.DateTime;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.vedant.notification_service.constants.ElasticsearchConstants;
import com.vedant.notification_service.exception.BadRequestException;
import com.vedant.notification_service.exception.IncorrectDateFormatException;
import com.vedant.notification_service.exception.InternalServerException;
import com.vedant.notification_service.exception.PageNumberNegativeException;
import com.vedant.notification_service.model.entity.ElasticsearchSms;
import com.vedant.notification_service.model.entity.SmsRequest;
import com.vedant.notification_service.model.requestBody.GetSmsBetweenDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class ElasticsearchService {
    final
    ElasticsearchClient esClient;

    public ElasticsearchService(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    public void saveData(SmsRequest sms){
        try{
            Timestamp start = new Timestamp(sms.getCreated_at().toEpochSecond(ZoneOffset.ofHoursMinutes(5,30)));
            Timestamp end = new Timestamp(sms.getUpdated_at().toEpochSecond(ZoneOffset.ofHoursMinutes(5,30)));
            ElasticsearchSms esSms = new ElasticsearchSms(sms.getId(), sms.getPhone_number(), sms.getMessage(), start,end);
            IndexResponse response = esClient.index(i -> i
                    .index(ElasticsearchConstants.INDEX_NAME)
                    .id(String.valueOf(esSms.getId()))
                    .document(esSms)
            );
            System.out.println("Indexed with version " + response.version());
            System.out.println(response.result());
        } catch (Exception e){
            throw new InternalServerException("Unknown error occurred. Could not save data to ES!");
        }
    }

    public Object getSmsBetweenDates(GetSmsBetweenDate request){
        String startDate = request.getStartDate();
        String endDate = request.getEndDate();
        int pageNumber = request.getPageNumber();
        String phoneNumber = request.getPhoneNumber();
        if(pageNumber < 0){
            throw new PageNumberNegativeException();
        }
        if(startDate == null || endDate == null || phoneNumber == null || phoneNumber.isEmpty()){
            throw new BadRequestException("Insufficient Data provided.");
        }
        try{
            DateTimeFormatter formatter = (DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime start = LocalDateTime.parse(startDate, formatter);
            LocalDateTime end = LocalDateTime.parse(endDate, formatter);

            System.out.println("start time: " + start.toEpochSecond(ZoneOffset.ofHoursMinutes(5,30)));
            System.out.println("end time: " + end.toEpochSecond(ZoneOffset.ofHoursMinutes(5,30)));

            Query byPhoneNumber = MatchQuery.of(m -> m
                    .field("phone_number")
                    .query(phoneNumber)
            )._toQuery();
            Query byDate = RangeQuery.of(m -> m
                    .field("created_at")
                    .gte(JsonData.of(start.toEpochSecond(ZoneOffset.ofHoursMinutes(5,30))))
                    .lte(JsonData.of(end.toEpochSecond(ZoneOffset.ofHoursMinutes(5,30))))
            )._toQuery();



            SearchResponse<ElasticsearchSms> response = esClient.search(s -> s
                            .index(ElasticsearchConstants.INDEX_NAME)
                            .size(ElasticsearchConstants.PAGE_SIZE)
                            .from(pageNumber * ElasticsearchConstants.PAGE_SIZE)
                            .query(q -> q
                                    .bool(b -> b
                                            .must(byPhoneNumber)
                                            .must(byDate)
                                    )
                            ),
                    ElasticsearchSms.class
            );
            System.out.println(response);
            if(response.timedOut()){
                throw new InternalServerException("Elastic Search timed out!");
            }
//            System.out.println(response.hits());
//            System.out.println(response.hits().hits());
            List<ElasticsearchSms> result = response.hits().hits().stream().map(Hit::source).toList();
            System.out.println(result);
            return result;
        }catch (DateTimeParseException ex){
            System.out.println("Date Time Parse Error");
            throw new IncorrectDateFormatException();
        } catch (Exception e){
            System.out.println("Exception caught: " + e);
            throw new InternalServerException("Error communicating with ElasticSearch");
        }
    }

    public Object getSmsWithText(String searchText, int pageNumber) {
        List<ElasticsearchSms> result = null;


        if(searchText == null || searchText.isEmpty()){
            throw new BadRequestException("Insufficient Data provided.");
        }
        if(pageNumber < 0){
            throw new PageNumberNegativeException();
        }

        try{
            SearchResponse<ElasticsearchSms> response = esClient.search(s -> s
                            .index(ElasticsearchConstants.INDEX_NAME)
                            .size(ElasticsearchConstants.PAGE_SIZE)
                            .from(pageNumber * ElasticsearchConstants.PAGE_SIZE)
                            .query(q -> q
                                    .match(t -> t
                                            .field("message")
                                            .query(searchText)
                                    )
                            ),
                    ElasticsearchSms.class
            );
            result = response.hits().hits().stream().map(Hit::source).toList();
            System.out.println(result);
        } catch (Exception e){
            System.out.println("Exception caught: " + e);
            throw new InternalServerException("Error communicating with ElasticSearch");
        }
        return result;
    }

    public void deleteIndex(){
        try{
            esClient.indices().delete(d -> d.index(ElasticsearchConstants.INDEX_NAME));
        } catch (Exception e){
            System.out.println("Error deleting index. " + e);
        }
    }

    public void createIndex(){
        try{
            esClient.indices().create(c -> c.index(ElasticsearchConstants.INDEX_NAME));
        } catch (Exception e){
            System.out.println("Error creating index. " + e);
        }
    }
}
