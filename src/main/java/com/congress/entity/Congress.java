package com.congress.entity;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Congress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Size(min = 3, max = 256)
	private String name;
	@Size(min = 3, max = 256)
	private String logo_url;
	@Transient
	private MultipartFile logo;
	@Size(min = 3, max = 7)
	@Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
	private String color;
	@Transient
	private MultipartFile banner;
	@Size(min = 3, max = 256)
	private String banner_url;
	// private String[] networks;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	@Size(min = 3, max = 256)
	private String location;
	@Size(min = 3, max = 256)
	private String registration;
	@Size(min = 3, max = 256)
	private String poster;

	@Column(length = 1000)
	private String description;

	@OneToMany
	private List<SocialLink> socialLinks;
	public Congress() {
		this.socialLinks = new ArrayList<>();
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	// getters and Setters
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public MultipartFile getLogo() {
		return logo;
	}

	public String getColor() {
		return color;
	}

	public void setLogo(MultipartFile logo) {
		this.logo = logo;
	}

	public String getLogo_url() {
		return logo_url;
	}

	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}

	public String getLocation() {
		return location;
	}

	public String getRegistration() {
		return registration;
	}

	public String getPoster() {
		return poster;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * public String[] getNetworks() { return networks; }
	 */
	public Date getDate() {
		return date;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBanner_url() {
		return banner_url;
	}

	public void setBanner_url(String banner_url) {
		this.banner_url = banner_url;
	}

	/*
	 * public void setNetworks(String[] networks) { this.networks = networks; }
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public MultipartFile getBanner() {
		return banner;
	}

	public void setBanner(MultipartFile banner) {
		this.banner = banner;
	}

	public void addSocialLink(SocialLink currentSocialLink) {
		// TODO Auto-generated method stu
		currentSocialLink.setCongress(this);
		this.socialLinks.add(currentSocialLink);
	}
	public void removeSocialLink(SocialLink toDelete) {
		toDelete.setCongress(null);
		this.socialLinks.remove(toDelete);
	}
	
}
