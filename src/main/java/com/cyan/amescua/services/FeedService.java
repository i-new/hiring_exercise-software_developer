package com.cyan.amescua.services;

import com.cyan.amescua.model.Feed;
import com.cyan.amescua.providers.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedService {

    @Autowired
    private FeedRepository feedRepository;

    private List<List<Feed>> feedsList = new ArrayList<List<Feed>>();

    private List<List<String>> words = new ArrayList<List<String>>();

    private List<String> allFeedWords = new ArrayList<String>();
    private List<String> repeatedWords = new ArrayList<String>();

    private static Integer currentFeed = 0;

    public List<String> retrieveRSS(List<String> urls) {
        return this.analyseFeeds(urls);
    }

    // method to filter topics using the arrays
    // MOVE TO FEED SERVICE

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
    private List<String> analyseFeeds(List<String> feeds) {
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

        // clean all the pronoms and adjevtives from the final array
        return repeatedWords;
    }

    private void nextFeed() {
        currentFeed++;
    }

    private void resetFeedCounter() {
        currentFeed = 0;
    }
}
