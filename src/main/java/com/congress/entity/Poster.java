package com.congress.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class Poster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Size(min = 3, max = 256)
	private String poster_url;

	public long getId() {
		return id;
	}

	public String getPoster_url() {
		return poster_url;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setPoster_url(String poster_url) {
		this.poster_url = poster_url;
	}

}
