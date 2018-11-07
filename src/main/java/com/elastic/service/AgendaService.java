package com.elastic.service;

import com.elastic.elasticsearch.ElasticSearchResponse;
import com.elastic.model.Agenda;
import com.elastic.repository.AgendaRepository;
import com.google.gson.Gson;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AgendaService {

    private static final Logger LOGGER = Logger.getLogger(indexService.class);

    private AgendaRepository agendaRepository;
    private RestClient restClient;
    private Gson gson;

    @Autowired
    public AgendaService(AgendaRepository agendaRepository, RestClient restClient) {
        this.agendaRepository = agendaRepository;
        this.restClient = restClient;
        this.gson = new Gson();
    }

    public void agendaPost(Agenda request) {
        String params = gson.toJson(request);
        requestPostAgenda(request, params);
    }

    public ElasticSearchResponse getSearch() {
        return getElasticSearchResponse();
    }

    private ElasticSearchResponse getElasticSearchResponse() {
        try (NStringEntity entity = new NStringEntity("", ContentType.APPLICATION_JSON)) {
            Response response = restClient.performRequest("GET", "/agenda/doc/_search", new HashMap<>(), entity);
            String json = EntityUtils.toString(response.getEntity());

            ElasticSearchResponse results = gson.fromJson(json, ElasticSearchResponse.class);
            return results;

        } catch (Exception e) {
            LOGGER.error("Erro ao buscar os  dados no ElasticSearch.", e);
            throw new RuntimeException(e);
        }
    }

    private void requestPostAgenda(Agenda request, String params) {
        try (NStringEntity entity = new NStringEntity(params, ContentType.APPLICATION_JSON)) {
            agendaRepository.save(request);
            restClient.performRequest("POST", "/agenda/doc", new HashMap<>(), entity);

        } catch (Exception e) {
            LOGGER.error("Erro ao criar dados no elastic.", e);
            throw new RuntimeException(e);
        }
    }
}
