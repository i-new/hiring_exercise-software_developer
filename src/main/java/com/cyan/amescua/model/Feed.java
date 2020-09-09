package com.cyan.amescua.model;

import javax.persistence.*;

@Entity
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String description;


}