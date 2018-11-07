package com.elastic.elasticsearch;

import com.elastic.service.indexService;
import com.google.gson.Gson;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class ElasticSearchApi<T> {

    private static final Logger LOGGER = Logger.getLogger(indexService.class);

    private static RestClient restClient;
    private static Gson gson;

    @Autowired
    public ElasticSearchApi(RestClient restClient) {
        this.restClient = restClient;
        this.gson = new Gson();
    }

    public void index(String authToken, String esAuthToken, T nameIndex) {

        if (esAuthToken.equals(authToken)) {

            // deleta o indice já exite e cria novamente.
            try (NStringEntity entity = new NStringEntity("", ContentType.APPLICATION_JSON)) {
                restClient.performRequest("DELETE", "/" + nameIndex, new HashMap<>(), entity);
                //cria o indice
                restClient.performRequest("PUT", "/" + nameIndex, new HashMap<>(), entity);

                //Se o deletar falhar ele cria o indice
            } catch (Exception e) {
                LOGGER.error("Erro ao deletar o indice no ES.", e);
                // cria o indice.
                try (NStringEntity entity = new NStringEntity("", ContentType.APPLICATION_JSON)) {
                    restClient.performRequest("PUT", "/" + nameIndex, new HashMap<>(), entity);
                } catch (Exception ex) {
                    LOGGER.error("Erro ao criar o indice no ES.", ex);
                    throw new RuntimeException(e);
                }
            }
            return;
        }
        throw new RuntimeException("Token incorreto.");
    }

    public void excluiIndex(String authToken, String esAuthToken, T request) {

        if (esAuthToken.equals(authToken)) {

            // deleta o indice .
            try (NStringEntity entity = new NStringEntity("", ContentType.APPLICATION_JSON)) {
                restClient.performRequest("DELETE", "/" + request, new HashMap<>(), entity);


            } catch (Exception e) {
                LOGGER.error("Erro ao deletar o indice no ES.", e);
                throw new RuntimeException(e);
            }
            return;
        }
        throw new RuntimeException("Token incorreto.");
    }

    public static ElasticSearchResponse elasticSearch(String index) {
        try (NStringEntity entity = new NStringEntity("", ContentType.APPLICATION_JSON)) {
            Response response = restClient.performRequest("GET", "/"+index+"/doc/_search", new HashMap<>(), entity);
            String json = EntityUtils.toString(response.getEntity());

            ElasticSearchResponse results = gson.fromJson(json, ElasticSearchResponse.class);

            if(results!=null){
                return results;
            }
            return null;

        } catch (Exception e) {
            LOGGER.error("Erro ao buscar os  dados no ElasticSearch.", e);
            throw new RuntimeException(e);
        }
    }

    public ElasticSearchResponse elasticSearchPrefixe(String index, T request) {
        String params = "{\"query\" : {\"match_phrase_prefix\": " + gson.toJson(request) + " }}";
        try (NStringEntity entity = new NStringEntity(params, ContentType.APPLICATION_JSON)) {
            Response response = restClient.performRequest("GET", "/"+index+"/doc/_search", new HashMap<>(), entity);
            String json = EntityUtils.toString(response.getEntity());

            ElasticSearchResponse results = gson.fromJson(json, ElasticSearchResponse.class);
            return results;

        } catch (Exception e) {
            LOGGER.error("Erro ao buscar os  dados no ElasticSearch.", e);
            throw new RuntimeException(e);
        }
    }

    public  void elasticCreate(String index, T request,    Long id) {
        String params = gson.toJson(request);
        try (NStringEntity entity = new NStringEntity(params, ContentType.APPLICATION_JSON)) {
            restClient.performRequest("POST", "/"+index+"/doc/"+id+"", new HashMap<>(), entity);

        } catch (Exception e) {
            LOGGER.error("Erro ao criar dados no elastic.", e);
            throw new RuntimeException(e);
        }
    }

    public  ElasticUpdateReturn elasticUpdade(String index, T request, Long id) {
        String params = "{\"doc\" : " + gson.toJson(request) +"}";
        try (NStringEntity entity = new NStringEntity(params, ContentType.APPLICATION_JSON)) {
            Response response = restClient.performRequest("POST", "/"+index+"/doc/"+id+"/_update", new HashMap<>(), entity);
            String json = EntityUtils.toString(response.getEntity());

            ElasticUpdateReturn results = gson.fromJson(json, ElasticUpdateReturn.class);
            return results;

        } catch (Exception e) {
            LOGGER.error("Erro ao buscar os  dados no ElasticSearch.", e);
            throw new RuntimeException(e);
        }
    }

    public  ElasticDeleteReturn elasticDeleteAttribute(String index, Long id) {

        try (NStringEntity entity = new NStringEntity("", ContentType.APPLICATION_JSON)) {
            Response response = restClient.performRequest("DELETE", "/"+index+"/doc/"+id+"", new HashMap<>(), entity);
            String json = EntityUtils.toString(response.getEntity());

            ElasticDeleteReturn results = gson.fromJson(json, ElasticDeleteReturn.class);
            return results;

        } catch (Exception e) {
            LOGGER.error("Erro ao buscar os  dados no ElasticSearch.", e);
            throw new RuntimeException(e);
        }
    }
}
