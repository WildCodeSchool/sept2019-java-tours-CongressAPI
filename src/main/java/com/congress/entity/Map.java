package com.congress.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * voila voila
 */

@Data
@Entity
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Size(min = 2, max = 256)
    @NotNull
    private String title;

    @Column(length=500)
    private String url;

    @ManyToOne
    @JsonBackReference
    private Congress congress;
}
