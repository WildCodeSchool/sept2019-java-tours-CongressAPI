package com.congress.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
	@NotNull
	private String name;

	private String logo_url;

	@Size(min = 3, max = 7)
	@Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
	@NotNull
	private String color;

	private String banner_url;
	// private String[] networks;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull
	private Date date;
	@Size(min = 3, max = 256)
	@NotNull
	private String location;
	@Size(min = 3, max = 256)
	@NotNull
	private String registration;
	@Size(min = 3, max = 256)
	@NotNull
	private String poster;
	@Size(min = 3, max = 256)
	@NotNull
	private String description;

	@Transient
	@JsonIgnore
	private MultipartFile banner;
	@Transient
	@JsonIgnore
	private MultipartFile logo;

	@OneToMany
	private List<About> abouts;


	@OneToMany
	private List<SocialLink> socialLinks;

	public Congress() {
		this.abouts = new ArrayList<>();
	}

	public void addAbout(About about) {
		about.setCongress(this);
		this.abouts.add(about);
	}

	public void removeAbout(About toDelete) {
		toDelete.setCongress(null);
		this.abouts.remove(toDelete);
	}

	public void addSocialLink(SocialLink socialLink) {
		socialLink.setCongress(this);
		this.socialLinks.add(socialLink);
	}

	public void removeSocialLink(SocialLink toDelete) {
		toDelete.setCongress(null);
		this.abouts.remove(toDelete);
	}
}
