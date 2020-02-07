package com.congress.controller.api;

import com.congress.entity.SocialLink;
import com.congress.services.SocialLinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/socialLink")
public class ApiSocialLinkController {

    private final SocialLinkService service;

    public ApiSocialLinkController(SocialLinkService service){
        this.service = service;
    }
    @GetMapping
    List<SocialLink> all(){
        return service.findAll();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<SocialLink> createSocialLink(SocialLink socialLink){
        SocialLink savedCongress = service.create(socialLink);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCongress.getLogo()).toUri();
        return ResponseEntity.created(location).build();
    }
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<SocialLink> updateSocialLink(SocialLink socialLink) throws Exception{
        return ResponseEntity.ok(service.update(socialLink));
    }
    @DeleteMapping("/{id}")
    public void deleteSocialLink(@PathVariable long id) throws Exception{
        service.delete(id);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SocialLink> getSocialLink(@PathVariable long id) throws Exception {
        return ResponseEntity.ok(service.findById(id));
    }
}
