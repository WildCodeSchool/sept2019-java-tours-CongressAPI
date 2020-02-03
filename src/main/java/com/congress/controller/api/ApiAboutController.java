package com.congress.controller.api;

import com.congress.entity.About;
import com.congress.entity.Congress;
import com.congress.services.AboutService;
import com.congress.services.CongressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/congress/{congressId}/about")
public class ApiAboutController {
    private final AboutService service;
    private final CongressService congressService;

    public ApiAboutController(AboutService service, CongressService congressService) {
        this.service = service;
        this.congressService = congressService;
    }

    @GetMapping
    List<About> all(@PathVariable long congressId) {
        return service.findByCongressId(congressId);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<About> createSponsor(@PathVariable long congressId, About about) throws Exception {
        Congress currentCongress = congressService.findById(congressId);
        currentCongress.addAbout(about);
        About savedCongress = service.create(about);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCongress.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<About> updateAbout(About about) throws Exception {
        return ResponseEntity.ok(service.update(about));
    }

    @DeleteMapping("/{id}")
    public void deleteAbout(@PathVariable long id) throws Exception {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<About> getAbout(@PathVariable long id) throws Exception {
        return ResponseEntity.ok(service.findById(id));
    }
}
