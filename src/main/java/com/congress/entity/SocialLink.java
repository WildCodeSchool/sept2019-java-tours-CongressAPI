package com.congress.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SocialLink {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String logo_url;
	
	private String social_link_url;

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
	

}
