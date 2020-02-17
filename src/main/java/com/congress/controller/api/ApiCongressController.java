package com.congress.controller.api;

import com.congress.entity.Congress;
import com.congress.services.CongressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/congress")
public class ApiCongressController {

    private final CongressService service;

    public ApiCongressController(CongressService service) {
        this.service = service;
    }


    @GetMapping
    List<Congress> all() {
        return service.findAll();
    }

    /**
     * This controller is used to create a congress
     *
     * @param congress The congress to create
     * @return Created congress
     */
    @PostMapping(consumes = {"multipart/form-data", "application/json"}, headers = "content-type=application/json")
    @Valid
    public ResponseEntity<Congress> createCongress(@ModelAttribute Congress congress,
                                                   @RequestPart(value = "logo", required = true) MultipartFile logo,
                                                   @RequestPart(value = "banner", required = true) MultipartFile banner) {
        Congress savedCongress = service.create(congress, logo, banner);
        return ResponseEntity.ok(savedCongress);
    }

    /**
     * This controller is used to update a congress
     *
     * @param newCongress The model of the congress
     * @return Redirect to the congress view
     */
    @PutMapping(consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<Congress> updateCongress(@ModelAttribute("congress") Congress newCongress,
                                                   @RequestPart(value = "logo", required = true) MultipartFile logo,
                                                   @RequestPart(value = "banner", required = true) MultipartFile banner) throws Exception {
        return ResponseEntity.ok(service.update(newCongress.getId(), logo, banner));
    }

    /**
     * This controller is used to delete a congress
     *
     * @param id The id of the congress
     * @return Redirect to the home page
     */
    @DeleteMapping("/{id}")
    public void deleteCongress(@PathVariable long id) throws Exception {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Congress> getCongress(@PathVariable long id) throws Exception {
        return ResponseEntity.ok(service.findById(id));
    }
}
