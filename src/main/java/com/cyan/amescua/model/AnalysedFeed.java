package com.cyan.amescua.model;

import javax.persistence.*;

/**
 * This class will stored the analysed hot topics from RSS feeds in our database.
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
    private String rssLinks;

    public AnalysedFeed() {

    }

    public AnalysedFeed(String hotTopics, String rssLinks) {
        this.hotTopics = hotTopics;
        this.rssLinks = rssLinks;
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

    public String getRssLinks() {
        return rssLinks;
    }

    public void setRssLinks(String rssLinks) {
        this.rssLinks = rssLinks;
    }

    @Override
    public String toString() {
        return "AnalysedFeed{" +
                "id=" + id +
                ", hotTopics='" + hotTopics + '\'' +
                ", rssLinks='" + rssLinks + '\'' +
                '}';
    }
}
