package com.bigbro.wwooss.v1.job;//package com.bigbro.killbill.v1.config;
//
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
//import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//
//@Configuration
//@EnableElasticsearchRepositories
//public class ElasticsearchConfig extends AbstractElasticsearchConfiguration  {
//    @Value("${spring.elasticsearch.uris}")
//    private String esUris;
//
//    @Override
//    public RestHighLevelClient elasticsearchClient() {
//        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo(esUris)
//                .build();
//        return RestClients.create(clientConfiguration).rest();
//    }
//}
