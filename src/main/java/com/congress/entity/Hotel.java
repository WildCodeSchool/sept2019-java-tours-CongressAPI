package com.congress.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String url;

    @ManyToMany(mappedBy = "hotels")
    @JsonBackReference
    private Set<Congress> congress;

    public void addCongress(Congress congress) {
        congress.addHotel(this);
        this.congress.add(congress);
    }

    public void removeCongress(Congress toDelete) {
        toDelete.removeHotel(this);
        this.congress.remove(toDelete);
    }
}
