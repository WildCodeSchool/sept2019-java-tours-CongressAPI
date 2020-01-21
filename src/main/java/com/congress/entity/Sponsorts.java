package com.congress.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@Entity
public class Sponsorts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Size(min = 3, max = 255)
    private String name;

    private String logo_url;
    @Transient
    @JsonIgnore
    private MultipartFile logo;

    private String location;
    private String city;
    private String contact;
    private String address;
    private int phone;
    private String mail;
    private String website_url;

    @ManyToOne
    private Congress congress;

}