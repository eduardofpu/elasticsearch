package com.elastic.service;

import com.elastic.elasticsearch.ElasticSearchApi;
import com.elastic.model.Index;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class indexService extends ElasticSearchApi {


    @Value("${elasticsearch.admin.authtoken}")
    private String esAuthToken;

    public indexService(RestClient restClient) {
        super(restClient);
    }

    public void createIndex(String esAuthToken, Index request) {
        index(this.esAuthToken, esAuthToken, request.getIndexName());
    }

    public void deleteIndex(String esAuthToken, Index request) {
        excluiIndex(this.esAuthToken, esAuthToken, request.getIndexName());
    }
}
