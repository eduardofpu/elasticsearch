package com.elastic.service;

import com.elastic.elasticsearch.ElasticDeleteReturn;
import com.elastic.elasticsearch.ElasticUpdateReturn;
import com.elastic.elasticsearch.ElasticSearchResponse;
import com.elastic.model.Agenda;
import com.elastic.model.AgendaRequest;
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

    private RestClient restClient;
    private Gson gson;

    @Autowired
    public AgendaService(RestClient restClient) {
        this.restClient = restClient;
        this.gson = new Gson();
    }

    public void agendaCreate(Agenda request) {
        String params = gson.toJson(request);
        requestPostAgenda(request, params);
    }

    public ElasticSearchResponse agendaSearch() {
        return getElasticSearch();
    }


    public ElasticSearchResponse agendaSearchPrefix(AgendaRequest request) {

        String params = "{\"query\" : {\"match_phrase_prefix\": " + gson.toJson(request) + " }}";
        return getElasticSearchPrefixe(params);
    }

    public ElasticUpdateReturn agendaUpdate(AgendaRequest request, Long id) {

        String params = "{\"doc\" : " + gson.toJson(request) +"}";
        return getElasticUpdade(params, id);
    }

    public ElasticDeleteReturn agendaUpdate(Long id) {
        return getElasticDelete(id);
    }



    private ElasticSearchResponse getElasticSearch() {
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

    private ElasticSearchResponse getElasticSearchPrefixe(String params) {

        try (NStringEntity entity = new NStringEntity(params, ContentType.APPLICATION_JSON)) {
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
            restClient.performRequest("POST", "/agenda/doc/"+request.getId(), new HashMap<>(), entity);

        } catch (Exception e) {
            LOGGER.error("Erro ao criar dados no elastic.", e);
            throw new RuntimeException(e);
        }
    }

    private ElasticUpdateReturn getElasticUpdade(String params, Long id) {

        try (NStringEntity entity = new NStringEntity(params, ContentType.APPLICATION_JSON)) {
            Response response = restClient.performRequest("POST", "/agenda/doc/"+id+"/_update", new HashMap<>(), entity);
            String json = EntityUtils.toString(response.getEntity());

            ElasticUpdateReturn results = gson.fromJson(json, ElasticUpdateReturn.class);
            return results;

        } catch (Exception e) {
            LOGGER.error("Erro ao buscar os  dados no ElasticSearch.", e);
            throw new RuntimeException(e);
        }
    }


    private ElasticDeleteReturn getElasticDelete(Long id) {

        try (NStringEntity entity = new NStringEntity("", ContentType.APPLICATION_JSON)) {
            Response response = restClient.performRequest("DELETE", "/agenda/doc/"+id+"", new HashMap<>(), entity);
            String json = EntityUtils.toString(response.getEntity());

            ElasticDeleteReturn results = gson.fromJson(json, ElasticDeleteReturn.class);
            return results;

        } catch (Exception e) {
            LOGGER.error("Erro ao buscar os  dados no ElasticSearch.", e);
            throw new RuntimeException(e);
        }
    }
}
