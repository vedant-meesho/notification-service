package com.vedant.notification_service.config;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.vedant.notification_service.constants.ElasticsearchConstants;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Configuration
@ComponentScan(basePackages = {"com.vedant.notification_service.service"})
public class ElasticsearchConfig
{
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        HttpHost httpHost = new HttpHost(ElasticsearchConstants.HOST_NAME, ElasticsearchConstants.PORT_NUMBER, ElasticsearchConstants.SCHEME);

        String fingerprint = ElasticsearchConstants.CERTIFICATE_FINGERPRINT;

        SSLContext sslContext = TransportUtils
                .sslContextFromCaFingerprint(fingerprint);

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(ElasticsearchConstants.USER, ElasticsearchConstants.USER_PASSWORD));
        RestClientBuilder restClientBuilder = RestClient.builder(httpHost).setHttpClientConfigCallback(hc -> hc
                        .setSSLContext(sslContext)
                        .setDefaultCredentialsProvider(credentialsProvider)
                );

        ElasticsearchTransport transport =
                new RestClientTransport(restClientBuilder.build(), new JacksonJsonpMapper());
        ElasticsearchClient client = new ElasticsearchClient(transport);
        System.out.println("ElasticsearchClient created {}"+client);
        return client;
    }
}

//
//@Configuration
//public class ElasticsearchConfig
//{
//    @Bean
//    public ElasticsearchClient elasticsearchClient() {
//        String serverUrl = "https://localhost:9200";
//        String apiKey = "WDRTLWFJMEJZOUxNUW16TDRJRXI6VUs1NEtmTkRROXlvZk93R3h1RUpXUQ==";
//
//// Create the low-level client
//        RestClient restClient = RestClient
//                .builder(HttpHost.create(serverUrl))
//                .setDefaultHeaders(new Header[]{
//                        new BasicHeader("Authorization", "ApiKey " + apiKey)
//                })
//                .build();
//// Create the transport with a Jackson mapper
//        ElasticsearchTransport transport = new RestClientTransport(
//                restClient, new JacksonJsonpMapper());
//
//// And create the API client
//        ElasticsearchClient esClient = new ElasticsearchClient(transport);
//        return esClient;
//    }
//}
