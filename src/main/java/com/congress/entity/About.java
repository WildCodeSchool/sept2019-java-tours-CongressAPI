package com.congress.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class About {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    @Column(length = 1000)
    private String description;

    @ManyToOne
    private Congress congress;
}
