package com.elastic.service;

import com.elastic.model.Index;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.HashMap;

@Service
public class indexService {

    private static final Logger LOGGER = Logger.getLogger(indexService.class);

    @Value("${elasticsearch.admin.authtoken}")
    private String esAuthToken;

    private RestClient restClient;

    @Autowired
    public indexService(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Metodo que ira refazer um indice no elastic search.
     * Inicialmente valida se o token eh valido
     *
     * @param esAuthToken
     * @param request
     */
    public void createIndex(String esAuthToken, Index request) {

        if (esAuthToken.equals(this.esAuthToken)) {

            // deleta o indice já exite e cria novamente.
            try (NStringEntity entity = new NStringEntity("", ContentType.APPLICATION_JSON)) {
                restClient.performRequest("DELETE", "/" + request.getIndexName(), new HashMap<>(), entity);
                //cria o indice
                restClient.performRequest("PUT", "/" + request.getIndexName(), new HashMap<>(), entity);

                //Se o deletar falhar ele cria o indice
            } catch (Exception e) {
                LOGGER.error("Erro ao deletar o indice no ES.", e);
                // cria o indice.
                try (NStringEntity entity = new NStringEntity("", ContentType.APPLICATION_JSON)) {
                    restClient.performRequest("PUT", "/" + request.getIndexName(), new HashMap<>(), entity);
                } catch (Exception ex) {
                    LOGGER.error("Erro ao criar o indice no ES.", ex);
                    throw new RuntimeException(e);
                }
            }
            return;
        }
        throw new RuntimeException("Token incorreto.");
    }

    public void deleteIndex(String esAuthToken, Index request) {

        if (esAuthToken.equals(this.esAuthToken)) {

            // deleta o indice .
            try (NStringEntity entity = new NStringEntity("", ContentType.APPLICATION_JSON)) {
                restClient.performRequest("DELETE", "/" + request.getIndexName(), new HashMap<>(), entity);


            } catch (Exception e) {
                LOGGER.error("Erro ao deletar o indice no ES.", e);
                throw new RuntimeException(e);
            }
            return;
        }
        throw new RuntimeException("Token incorreto.");
    }
}
