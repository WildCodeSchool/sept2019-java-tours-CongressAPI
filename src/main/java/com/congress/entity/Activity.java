package com.congress.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany
    private Set<Speaker> speakers;

    @ManyToOne
    private Congress congress;

    @DateTimeFormat(pattern = "HH:mm")
    private Date startDate;
    @DateTimeFormat(pattern = "HH:mm")
    private Date endDate;
    private String name;
    private String location;

    public Activity() {
        this.speakers = new HashSet<>();
    }

    public void addSpeaker(Speaker speaker) {
        this.speakers.add(speaker);
    }

    public void removeSpeaker(Speaker toDelete) {
        this.speakers.remove(toDelete);
    }
}
