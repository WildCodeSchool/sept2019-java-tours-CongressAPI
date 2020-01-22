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
	private String logo_url;
	
	private String social_link_url;
	@ManyToOne
	private Congress congress;
	
	@Transient
	private MultipartFile logo;

	public long getId() {
		return id;
	}

	public String getLogo_url() {
		return logo_url;
	}

	public String getSocial_link_url() {
		return social_link_url;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}

	public void setSocial_link_url(String social_link_url) {
		this.social_link_url = social_link_url;
	}

	public MultipartFile getLogo() {
		return logo;
	}

	public void setLogo(MultipartFile logo) {
		this.logo = logo;
	}

	}

