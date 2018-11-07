package com.elastic.config;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;

@Configuration
@EnableTransactionManagement()
public class Config {
    private static final Logger LOGGER = Logger.getLogger(Config.class);


    @Value("${elasticsearch.url}")
    private String elasticSearchUrl;

    @Bean
    public RestClient restClient() {
        LOGGER.info("Abrindo conexão com o Elasticsearch.");
        String[] splitUrls = this.elasticSearchUrl.split(";");
        ArrayList<HttpHost> hostList = new ArrayList<>();
        for (String item : splitUrls) {
            String[] host = item.split(":");
            LOGGER.debug("Conectando ao servidor Elasticsearch: " + host[2]);
            hostList.add(new HttpHost(host[1], new Integer(host[2]), host[0]));
        }
        return RestClient.builder(hostList.toArray(new HttpHost[0]))
                .setRequestConfigCallback(builder -> builder.setConnectTimeout(5000).setSocketTimeout(60000))
                .setMaxRetryTimeoutMillis(60000)
                .build();
    }
}
