package com.cyan.amescua.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String link;
    private String pubDate;

    @Column(unique = true)
    private String description;

    public Feed() {

    }

    public Feed(String title, String link, String pubDate, String description) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", pubDate=" + pubDate +
                ", description='" + description + '\'' +
                '}';
    }
}