package com.elastic.controller;

import com.elastic.elasticsearch.ElasticDeleteReturn;
import com.elastic.elasticsearch.ElasticSearchResponse;
import com.elastic.elasticsearch.ElasticUpdateReturn;
import com.elastic.model.Agenda;
import com.elastic.model.AgendaRequest;
import com.elastic.repository.AgendaRepository;
import com.elastic.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/agenda")
public class AgendaController {

    private final String INDEX = "agenda";
    private AgendaService agendaService;
    private AgendaRepository agendaRepository;

    @Autowired
    public AgendaController(AgendaService agendaService, AgendaRepository agendaRepository) {
        this.agendaService = agendaService;
        this.agendaRepository = agendaRepository;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Agenda createAgenda(@RequestBody Agenda request) throws IOException {

        agendaRepository.save(request);
        this.agendaService.agendaCreate(INDEX, request, request.getId());
        return request;
    }


    @RequestMapping(path = "/search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ElasticSearchResponse agendaSearch() throws IOException {
        ElasticSearchResponse search = agendaService.agendaSearch(INDEX);
        return search;
    }

    @RequestMapping(path = "/prefix", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ElasticSearchResponse agendaSearchPrefix(@RequestBody AgendaRequest request) throws IOException {
        ElasticSearchResponse search = agendaService.agendaSearchPrefix(INDEX, request);
        return search;
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ElasticUpdateReturn agendaUpdate(@RequestBody AgendaRequest request, @RequestHeader Long id) throws IOException {
        ElasticUpdateReturn search = agendaService.agendaUpdate(INDEX, request, id);
        return search;
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ElasticDeleteReturn agendaDelete(@RequestHeader Long id) throws IOException {
        ElasticDeleteReturn search = agendaService.agendaUpdate(INDEX, id);
        return search;
    }
}
