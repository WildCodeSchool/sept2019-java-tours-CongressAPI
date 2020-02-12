package com.congress.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Sponsor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Size(min = 3, max = 255, message = "there is to much character or to few")
    private String name;

    private String logo_url;
    @Transient
    @JsonIgnore
    private MultipartFile logo;

    @Size(min = 3, max = 255, message = "there is to much character or to few")
    @NotNull
    private String location;
    @Size(min = 3, max = 255, message = "there is to much character or to few")
    @NotNull
    private String city;
    @Size(min = 3, max = 255, message = "there is to much character or to few")
    @NotNull
    private String contact;
    @Size(min = 3, max = 255, message = "there is to much character or to few")
    @NotNull
    private String address;
    @Size(min = 3, max = 30, message = "Number is not on good shape")
    @Pattern(regexp = "^((\\+\\d{1,3}(-| )?\\(?\\d\\)?(-| )?\\d{1,5})|(\\(?\\d{2,6}\\)?))(-| )?(\\d{3,4})(-| )?(\\d{4})(( x| ext)\\d{1,5}){0,1}$", message = " ")
    @NotNull
    private String phone;
    @Size(min = 5, max = 256, message = "there is to much character or to few")
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,3})$", message = " ")
    private String mail;
    private String website_url;

    @ManyToMany(mappedBy = "sponsors")
    @JsonBackReference
    private Set<Congress> congress;

    public Sponsor() {
        this.congress = new HashSet<>();
    }

    public void addCongress(Congress congress) {
        congress.addSponsor(this);
        this.congress.add(congress);
    }

    public void removeCongress(Congress toDelete) {
        toDelete.removeSponsor(this);
        this.congress.remove(toDelete);
    }

}