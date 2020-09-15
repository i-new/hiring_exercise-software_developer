package com.cyan.amescua.services;

import com.cyan.amescua.model.AnalysedFeed;
import com.cyan.amescua.model.Feed;
import com.cyan.amescua.model.StoredFeedModel;
import com.cyan.amescua.providers.FeedRepository;
import com.cyan.amescua.utils.HelperService;
import com.cyan.amescua.utils.XMLService;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This is the main Service of the Feed API, it  communicates between the Controller and the Database repository.
 */
@Service
public class FeedService {

    @Autowired
    private FeedRepository feedRepository;

    private List<List<Feed>> feedsList = new ArrayList<List<Feed>>();

    private List<List<String>> words = new ArrayList<List<String>>();

    private List<String> allFeedWords = new ArrayList<String>();
    private List<String> repeatedWords = new ArrayList<String>();

    private HashMap<Integer, Feed> topThreeFeeds = new HashMap<Integer, Feed>();

    private static Integer currentFeed = 0;

    private static String prepositions = "about ,above ,across ,after ,against ,among ,around ,at ,before ,behind ,below ,beside ,between ,by ,down ,during ,for ,from ,in ,inside ,into ,near ,of ,off ,on ,out ,over ,through ,to ,toward ,under ,up ,with";
    private static String pronouns = "I ,me ,we ,us ,you ,her ,him ,it ,it's ,this ,these ,that ,those ,what ,who ,which ,whom ,whose ,my ,your ,yours ,their ,hers ,himself ,herself ,itself ,themselves ,ourselves ,yourself ,yourselves ,anybody ,anyone ,anything ,each  ,either ,everybody ,everyone ,everything ,neither ,nobody ,no one ,nothing ,one ,somebody ,someone ,something ,both ,few ,many ,several ,all ,any ,most ,none ,some ,the ,a ,an ,and ,or";

    /**
     * Returns analysed feeds to the client in JSON format.
     * This is the main method called from the FeedController
     * @param urls
     * @return
     */
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
                }
            }
        }
        return feedWords;
    }

    /**
     * This method is used to filter the common words from all the feeds.
     * It's called in the main method from this service class.
     * @param feedWords
     */
    private void filterFeedWords(List<String> feedWords, int currentPos) {
        int controller;
        for (String word : feedWords) {
            controller = 0;
            for (int i = 0; i < this.words.size(); i++) {
                if (i == currentPos) {
                    continue;
                }

                List<String> l = this.words.get(i);
                if (l.contains(word)) {
                    controller++;
                }
            }
            if (!this.repeatedWords.contains(word) && controller == this.words.size()-1) {
                this.repeatedWords.add(word);
            }
        }
    }

    /**
     * Api that gets feeds and analyse them
     * @param feeds
     * @return the analysed results to the main method of this class
     */
    private Map<String, Object> analyseFeeds(List<String> feeds) {
        // loop through every feed and get the entire parse feed list
        for (String url : feeds) {
            feedsList.add(XMLService.parseFeeds(url));
        }

        // get words from each feed
        for (List<Feed> feedArray : feedsList) {
            this.words.add(getFeedWords(feedArray));
        }

        // filter feeds
        int current = 0;
        for (List<String> words : this.words) {
            filterFeedWords(words, current);
            current++;
        }

        resetFeedCounter();

        // filter prepositions and pronouns from the results
        cleanResults();

        // loop feeds and see which news matches most with the repeated words
        findTopFeeds();

        // order by date
        orderFeedsByDates();

        AnalysedFeed f = feedRepository.save(new AnalysedFeed(String.join(", ", repeatedWords), HelperService.toJson(topThreeFeeds.values())));

        Map res = new HashMap();
        res.put("Related news in all the feeds: ", repeatedWords);
        res.put("Results Data: ", "/frequency/" + f.getId());

        // reset service variables
        cleanArrays();

        return res;
    }

    /**
     * Second method used for the API, to find an feeds analysys by id in the database.
     * @param id
     * @return
     */
    public Map<String, Object> getFeedById(Long id) {
        Optional<AnalysedFeed> f = feedRepository.findById(id);

        Map<String, Object> res = new HashMap<>();

        if (!f.isPresent()) {
            res.put("message", "Object Not Found, there is not object matching this ID in the Database.");
        } else {
            AnalysedFeed analysedFeed = f.get();
            StoredFeedModel model = new StoredFeedModel(analysedFeed.getId(), analysedFeed.getHotTopics(), HelperService.jsonToList(analysedFeed.getTopFeeds()));
            res.put("AnalysedFeed", model);
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
        topThreeFeeds.clear();
    }

    /**
     * Order a feed's list by their publication date
     */
    private void orderFeedsByDates() {
        List<Long> miliSeconds = new ArrayList<Long>();
        HashMap<Long, Feed> feeds = new HashMap<>();
        for (Feed feed : topThreeFeeds.values()) {
            miliSeconds.add(HelperService.parseStringToDate(feed.getPubDate()).getTime());
            feeds.put(HelperService.parseStringToDate(feed.getPubDate()).getTime(), feed);
        }
        // order milliseconds
        miliSeconds.sort(Comparator.naturalOrder());

        topThreeFeeds.clear();
        for (int i = 0; i < miliSeconds.size(); i++) {
            topThreeFeeds.put(i, feeds.get(miliSeconds.get(i)));
        }
    }

    /**
     * It should give you the top three results feeds
     */
    private void findTopFeeds() {
        Integer count;
        HashMap<Integer, Feed> helper = new HashMap<Integer, Feed>();

        for (List<Feed> feedList : feedsList) {
            for (Feed feed : feedList) {
                count = 0; // reset count

                for (String word : repeatedWords) {
                    if (HelperService.isContain(feed.getTitle().toLowerCase(), word)) {
                        count++;
                    }
                }

                if (topThreeFeeds.size() == 0 && count > 0) {

                    topThreeFeeds.put(count, feed);

                } else {
                    // if is less than 3, but count > 0, add + order
                    if (topThreeFeeds.size() <  3 && count > 0) {
                        topThreeFeeds.put(count, feed);
                        sortFeeds();

                    // if is 3 already, check that new one is bigger than last one
                    } else if (topThreeFeeds.size() == 3 && count > 0) {
                        Integer lastValue = (Integer) topThreeFeeds.keySet().toArray()[topThreeFeeds.size()-1];

                        if (lastValue < count) {
                            // helper variables (take 2 and 3, clean the map, add 2 and 3 as the first ones, add the new one)
                            helper.put((Integer)topThreeFeeds.keySet().toArray()[1], topThreeFeeds.get(topThreeFeeds.keySet().toArray()[1]));
                            helper.put((Integer)topThreeFeeds.keySet().toArray()[2], topThreeFeeds.get(topThreeFeeds.keySet().toArray()[2]));

                            topThreeFeeds.clear(); // reset map

                            topThreeFeeds.put((Integer)helper.keySet().toArray()[0], helper.get(helper.keySet().toArray()[0]));
                            topThreeFeeds.put((Integer)helper.keySet().toArray()[1], helper.get(helper.keySet().toArray()[1]));
                            topThreeFeeds.put(count, feed); // we add the new bigger value

                            helper.clear();
                        }
                    }
                }
            }
        }
        System.out.println("Biggest: " + topThreeFeeds);
    }

    private void sortFeeds() {
        List<Integer> values = new ArrayList<Integer>();
        topThreeFeeds.keySet().forEach(key -> values.add(key));

        values.sort(Comparator.naturalOrder());

        HashMap<Integer, Feed> helper = new HashMap<Integer, Feed>();
        for (int i = 0 ; i < values.size(); i++) {
            helper.put((Integer)topThreeFeeds.keySet().toArray()[i], topThreeFeeds.get(topThreeFeeds.keySet().toArray()[i]));
        }

        topThreeFeeds.clear();
        topThreeFeeds = new HashMap<Integer, Feed>(helper);
    }

    /**
     * Cleans the retrieved common words of all Feeds from prepositions and pronouns
     */
    private void cleanResults() {
        String words = String.join(",", repeatedWords);

        for (String prep : prepositions.split(" ,")) {
            words = words.replaceAll("\\b"+prep+",\\b", "");
        }

        for (String pron : pronouns.split(" ,")) {
            words = words.replaceAll("\\b"+pron+",\\b", "");
        }

        repeatedWords = Arrays.asList(words.split(","));
    }
}
