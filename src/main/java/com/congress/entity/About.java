package com.congress.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
public class About {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Size(min = 3, max = 256)
    @NotNull
    private String title;
    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JsonBackReference
    private Congress congress;
}
