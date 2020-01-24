package com.congress.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
@Data
@Entity
public class SocialLink {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Size(min = 3, max = 256)
	private String logoUrl;
	
	private String socialLinkUrl;
	@ManyToOne
	private Congress congress;
	
	@Transient
	private MultipartFile logo;



	}

