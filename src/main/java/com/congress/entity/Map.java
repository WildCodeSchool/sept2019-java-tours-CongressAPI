package com.congress.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String url;

    @ManyToOne
    private Congress congress;
}
