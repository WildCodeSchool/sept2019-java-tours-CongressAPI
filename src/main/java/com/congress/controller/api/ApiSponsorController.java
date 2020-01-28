package com.congress.controller.api;

import com.congress.entity.Sponsor;
import com.congress.services.SponsorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sponsor")
public class ApiSponsorController {

    private final SponsorService service;

    public ApiSponsorController(SponsorService service) {
        this.service = service;
    }

    @GetMapping
    List<Sponsor> all() {
        return service.findAll();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<Sponsor> createSponsor(Sponsor sponsor) {
        Sponsor savedCongress = service.create(sponsor);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCongress.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<Sponsor> updateSponsor(Sponsor sponsor) throws Exception {
        return ResponseEntity.ok(service.update(sponsor));
    }

    @DeleteMapping("/{id}")
    public void deleteSponsor(@PathVariable long id) throws Exception {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sponsor> getSponsor(@PathVariable long id) throws Exception {
        return ResponseEntity.ok(service.findById(id));
    }
}
