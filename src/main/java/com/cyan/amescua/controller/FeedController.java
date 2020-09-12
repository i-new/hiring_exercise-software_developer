package com.cyan.amescua.controller;

import com.cyan.amescua.services.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FeedController {

    @Autowired
    private FeedService feedService;

    /**
     * Will return a list of /frequency/{id} endpoints for each given url
     * @param url1
     * @param url2
     * @return
     */
    @GetMapping(value = "/analyse/new")
    public String retrieveRSS(@RequestParam String url1, @RequestParam String url2) {
//        use the service to make the operations and save the data in database
        List<String> urls = new ArrayList<>();
        urls.add(url1);
        urls.add(url2);
        List<String> res = feedService.retrieveRSS(urls);
        System.out.println("There are news related to: " + res + " in both feeds.");

        return "hello analyse: " + url1 + " & " + url2; // /frequency/{id}
    }

    @GetMapping(value = "/frequency/{id}")
    public List<String> retrieveResults(@PathVariable("id") Long id) {
        // check this results by the service to get the data and retrieve them to the client
        return null;
    }
}
