package com.congress.controller.api;

import com.congress.entity.Congress;
import com.congress.entity.SocialLink;
import com.congress.services.CongressService;
import com.congress.services.SocialLinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/socialLink")
public class ApiSocialLinkController {

    private final SocialLinkService service;
    private final CongressService congressService;

    public ApiSocialLinkController(SocialLinkService service, CongressService congressService) {
        this.service = service;
        this.congressService = congressService;
    }

    @GetMapping
    List<SocialLink> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocialLink> getSocialLink(@PathVariable long id) throws Exception {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<SocialLink> createSocialLink(@PathVariable long congressId, SocialLink savedSocialLink) throws IOException {
        Congress currentCongress = congressService.findById(congressId);
        currentCongress.addSocialLink(savedSocialLink);
        SocialLink savedCongress = service.create(savedSocialLink);
        congressService.update(currentCongress);
        return ResponseEntity.ok(savedSocialLink);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<SocialLink> updateSocialLink(@PathVariable long congressId, SocialLink updatedSocialLink) throws Exception {
        Congress currentCongress = congressService.findById(congressId);
        currentCongress.addSocialLink(updatedSocialLink);
        SocialLink savedCongress = service.update(updatedSocialLink);
        congressService.update(currentCongress);
        return ResponseEntity.ok(updatedSocialLink);
    }

    @DeleteMapping("/{id}")
    public void deleteSocialLink(@PathVariable long id) throws Exception{
        service.delete(id);
    }

}
