package com.elastic.controller;

import com.elastic.model.Agenda;
import com.elastic.elasticsearch.ElasticSearchResponse;
import com.elastic.model.AgendaRequest;
import com.elastic.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/agenda")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Agenda createAgenda(@RequestBody Agenda request) throws IOException {

        this.agendaService.agendaPost(request);
        return request;
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ElasticSearchResponse getSearch() throws IOException {
        ElasticSearchResponse search = agendaService.getSearch();
        return search;
    }

    @RequestMapping(path = "/prefix", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ElasticSearchResponse getSearchPrefix(@RequestBody AgendaRequest request) throws IOException {
        ElasticSearchResponse search = agendaService.getSearchPrefix(request);
        return search;
    }
}
