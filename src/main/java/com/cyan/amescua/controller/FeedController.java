package com.cyan.amescua.controller;

import com.cyan.amescua.services.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class FeedController {

    @Autowired
    private FeedService feedService;

    /**
     * Will return a list of /frequency/{id} endpoints for each given url
     * @param url1
     * @param url2
     * @return a response with the common hot topics matches and the id path to retrieve the analyse data "/frequency/{id}"
     */
    @GetMapping(value = "/analyse/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity retrieveRSS(@RequestParam(required = true) String url1, @RequestParam(required = true) String url2) {
        List<String> urls = new ArrayList<>();
        urls.add(url1);
        urls.add(url2);

        Map<String, Object> res = feedService.retrieveRSS(urls);

        return new ResponseEntity(res, HttpStatus.OK);
    }

    /**
     * Find AnalysedData stored in the database
     * @param id
     * @return an object containing the hot topics analyzed from two RSS feed links.
     */
    @GetMapping(value = "/frequency/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity retrieveResults(@PathVariable(required = true) Long id) {

        Map<String, Object> f = feedService.getFeedById(id);

        if (f.containsKey("AnalysedFeed")) {
            return new ResponseEntity(f, HttpStatus.OK);
        } else {
            return new ResponseEntity(f, HttpStatus.NOT_FOUND);
        }
    }
}
