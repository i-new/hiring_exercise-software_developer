package com.cyan.amescua.services;

import com.cyan.amescua.providers.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedService {

    @Autowired
    private FeedRepository feedRepository;

    private List<String> wordsUrl1 = new ArrayList<>();
    private List<String> commonWords = new ArrayList<>();

    public String retrieveRSS(String url) {
        return null;
    }

    // method to filter topics using the arrays
}
