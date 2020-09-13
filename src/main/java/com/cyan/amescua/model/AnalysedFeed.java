package com.cyan.amescua.model;

import javax.persistence.*;

/**
 * This class will stored and retrieve the analysed hot topics from RSS feeds in our database.
 */
@Entity
public class AnalysedFeed {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(length = 1000)
    private String hotTopics;

    @Lob
    @Column(length = 500)
    private String topFeeds;

    public AnalysedFeed() {

    }

    public AnalysedFeed(String hotTopics, String topFeeds) {
        this.hotTopics = hotTopics;
        this.topFeeds = topFeeds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHotTopics() {
        return hotTopics;
    }

    public void setHotTopics(String hotTopics) {
        this.hotTopics = hotTopics;
    }

    public String getTopFeeds() {
        return topFeeds;
    }

    public void setTopFeeds(String topFeeds) {
        this.topFeeds = topFeeds;
    }

    @Override
    public String toString() {
        return "AnalysedFeed{" +
                "id=" + id +
                ", hotTopics='" + hotTopics + '\'' +
                ", rssLinks='" + topFeeds + '\'' +
                '}';
    }
}
