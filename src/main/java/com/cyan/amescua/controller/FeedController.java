package com.cyan.amescua.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FeedController {

    @GetMapping(value = "/analyse/new")
    public String retrieveRSS(@RequestParam String url1, @RequestParam String url2) {
        return null;
    }

    @GetMapping(value = "/frequency/{id}")
    public List<String> retrieveResults(@PathVariable("id") Long id) {
        return null;
    }
}
