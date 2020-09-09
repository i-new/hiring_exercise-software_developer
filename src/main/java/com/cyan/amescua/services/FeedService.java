package com.cyan.amescua.services;

import com.cyan.amescua.providers.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class FeedService {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private List<String> wordsUrl1 = new ArrayList<>();
    @Autowired
    private List<String> commonWords = new ArrayList<>();

    public String retrieveRSS(String url) {
        return null;
    }

    // method to filter topics using the arrays
}
