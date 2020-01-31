package com.congress.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Speaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Size(min = 3, max = 255)
    private String name;

    private String photo_url;
    @Transient
    @JsonIgnore
    private MultipartFile photo;

    private String biography;

    @ManyToMany(mappedBy = "speakers")
    private Set<Congress> congress;

    public Speaker(){
        this.congress = new HashSet<>();
    }
    public void addCongress(Congress congress){
        congress.addSpeaker(this);
        this.congress.add(congress);
    }
    public void removeCongress(Congress toDelete){
        toDelete.removeSpeaker(this);
        this.congress.remove(toDelete);
    }

}
