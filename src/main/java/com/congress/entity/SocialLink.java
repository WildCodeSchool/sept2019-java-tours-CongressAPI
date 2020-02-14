package com.congress.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
public class SocialLink {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String logoUrl;
	@Size(min = 3, max = 256, message = "There is too much character or to few")
	@NotNull
	private String name;
	private String url;

	@ManyToOne
	@JsonBackReference
	private Congress congress;

	@Transient
	private MultipartFile logo;

}

