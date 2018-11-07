package com.elastic.controller;

import com.elastic.model.Index;
import com.elastic.service.indexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/index")
public class indexController {

    @Autowired
    private indexService indexService;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Index remakeIndex(@RequestHeader String esAuthToken, @RequestBody Index request) throws IOException {

        this.indexService.createIndex(esAuthToken, request);
        return request;
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIndex(@RequestHeader String esAuthToken, @RequestBody Index request) throws IOException {

        this.indexService.deleteIndex(esAuthToken, request);
    }
}
