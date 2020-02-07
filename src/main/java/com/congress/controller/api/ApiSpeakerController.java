package com.congress.controller.api;

import com.congress.entity.Speaker;
import com.congress.services.SpeakerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/speaker")
public class ApiSpeakerController {

    private final SpeakerService speakerService;

    public ApiSpeakerController(SpeakerService speakerService){
        this.speakerService = speakerService;
    }
    @GetMapping
    List<Speaker> all(){
        return speakerService.findAll();
    }
    @PostMapping(consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<Speaker> createSpeaker(Speaker speaker){
        Speaker savedCongress = speakerService.create(speaker);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCongress.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<Speaker> updateSpeaker(Speaker speaker) throws Exception{
        return ResponseEntity.ok(speakerService.update(speaker));
    }
    @DeleteMapping("/{id}")
    public void deleteSpeaker(@PathVariable long id) throws Exception {
        speakerService.delete(id);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Speaker> getSpeaker(@PathVariable long id) throws Exception{
        return ResponseEntity.ok(speakerService.findById(id));
    }
}
