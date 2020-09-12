package com.cyan.amescua.services;

import com.cyan.amescua.model.AnalysedFeed;
import com.cyan.amescua.model.Feed;
import com.cyan.amescua.providers.FeedRepository;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FeedService {

    @Autowired
    private FeedRepository feedRepository;

    private List<List<Feed>> feedsList = new ArrayList<List<Feed>>();

    private List<List<String>> words = new ArrayList<List<String>>();

    private List<String> allFeedWords = new ArrayList<String>();
    private List<String> repeatedWords = new ArrayList<String>();

    private static Integer currentFeed = 0;

    private static String prepositions = "about ,above ,across ,after ,against ,among ,around ,at ,before ,behind ,below ,beside ,between ,by ,down ,during ,for ,from ,in ,inside ,into ,near ,of ,off ,on ,out ,over ,through ,to ,toward ,under ,up ,with";
    private static String pronouns = "I ,me ,we ,us ,you ,her ,him ,it ,it's ,this ,these ,that ,those ,what ,who ,which ,whom ,whose ,my ,your ,yours ,their ,hers ,himself ,herself ,itself ,themselves ,ourselves ,yourself ,yourselves ,anybody ,anyone ,anything ,each  ,either ,everybody ,everyone ,everything ,neither ,nobody ,no one ,nothing ,one ,somebody ,someone ,something ,both ,few ,many ,several ,all ,any ,most ,none ,some ,the, a, an, and, or";

    @JsonAnyGetter
    public Map<String, Object> retrieveRSS(List<String> urls) {
        return this.analyseFeeds(urls);
    }

    /**
     * Retrieve all the words from a feed
     * @return
     */
    private List<String> getFeedWords(List<Feed> feeds) {
        List<String> feedWords = new ArrayList<>();

        for (Feed feed : feeds) {
            String [] words = feed.getTitle().toLowerCase().split(" ");

            for (String word : words) {

                if (!feedWords.contains(word)) {
                    feedWords.add(word);
                    // here as strings (unique)
                }
            }
        }
        System.out.println("Words: " + feedWords);
        return feedWords;
    }

    private void filterFeedWords(List<String> feedWords) {
        nextFeed();

        for (String fw : feedWords) {

            if (!this.allFeedWords.contains(fw)) {
                this.allFeedWords.add(fw);

            } else if (!this.repeatedWords.contains(fw) && currentFeed == 2) {
                this.repeatedWords.add(fw);
            }
        }
    }

    /**
     * Api that gets feeds and analyse them
     * @param feeds
     * @return the analysed results to the client
     */
    private Map<String, Object> analyseFeeds(List<String> feeds) {
        // loop throgh every feed and get the entire parse feed list
        for (String url : feeds) {
            feedsList.add(XMLService.parseFeeds(url));
        }

        // get words from each feed (TEST)
        for (List<Feed> feedArray : feedsList) {
            this.words.add(getFeedWords(feedArray));
        }

        // filter feeds
        for (List<String> words : this.words) {
            filterFeedWords(words);
        }

        resetFeedCounter();

        // filter prepositions and pronouns from the results
        cleanResults();

        AnalysedFeed f = feedRepository.save(new AnalysedFeed(String.join(", ", repeatedWords), feeds.toString()));

        Map res = new HashMap();
        res.put("Related news in both feeds: ", repeatedWords);
        res.put("link api", "/frequency/" + f.getId());

        // reset service variables
        cleanArrays();

        return res;
    }

    public Map<String, Object> getFeedById(Long id) {
        Optional<AnalysedFeed> f = feedRepository.findById(id);

        Map<String, Object> res = new HashMap<>();

        if (!f.isPresent()) {
            res.put("message", "Object Not Found, there is not object matching this ID in the Database.");
        } else {
            res.put("AnalysedFeed", f);
        }

        return res;
    }

    private void nextFeed() {
        currentFeed++;
    }

    private void resetFeedCounter() {
        currentFeed = 0;
    }

    private void cleanArrays() {
        feedsList = new ArrayList<List<Feed>>();

        words = new ArrayList<List<String>>();

        allFeedWords = new ArrayList<String>();
        repeatedWords = new ArrayList<String>();
    }

    /**
     * Clean the analysed data from prepositions and pronoums
     */
    private void cleanResults() {
        String words = String.join(",", repeatedWords);
        System.out.println(words);

        for (String prep : prepositions.split(" ,")) {
            words = words.replaceAll("\\b"+prep+",\\b", "");
        }

        for (String pron : pronouns.split(" ,")) {
            words = words.replaceAll("\\b"+pron+",\\b", "");
        }

        repeatedWords = Arrays.asList(words.split(","));
        System.out.println("Cleaned: " + repeatedWords);
    }
}
