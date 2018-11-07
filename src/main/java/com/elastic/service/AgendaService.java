package com.elastic.service;

import com.elastic.elasticsearch.ElasticDeleteReturn;
import com.elastic.elasticsearch.ElasticSearchApi;
import com.elastic.elasticsearch.ElasticSearchResponse;
import com.elastic.elasticsearch.ElasticUpdateReturn;
import com.elastic.model.Agenda;
import com.elastic.model.AgendaRequest;
import org.apache.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;

@Service
public class AgendaService extends ElasticSearchApi{

    private static final Logger LOGGER = Logger.getLogger(indexService.class);


    public AgendaService(RestClient restClient) {
        super(restClient);
    }

    public void agendaCreate(String index, Agenda request,Long id) {
        elasticCreate(index, request, id);
    }

    public ElasticSearchResponse agendaSearch(String index) {
        return elasticSearch(index);
    }


    public ElasticSearchResponse agendaSearchPrefix(String index, AgendaRequest request) {
        return elasticSearchPrefixe(index, request);
    }

    public ElasticUpdateReturn agendaUpdate(String index, AgendaRequest request, Long id) {
        return elasticUpdade(index, request, id);
    }

    public ElasticDeleteReturn agendaUpdate(String index, Long id) {
        return elasticDeleteAttribute(index, id);
    }
}
