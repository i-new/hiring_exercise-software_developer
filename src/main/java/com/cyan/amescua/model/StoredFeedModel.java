package com.cyan.amescua.model;

import java.util.List;

public class StoredFeedModel {

    private Long id;
    private String hotTopics;
    private List<Feed> topFeeds;

    public StoredFeedModel(Long id, String hotTopics, List<Feed> topFeeds) {
        this.id = id;
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

    public List<Feed> getTopFeeds() {
        return topFeeds;
    }

    public void setTopFeeds(List<Feed> topFeeds) {
        this.topFeeds = topFeeds;
    }
}
