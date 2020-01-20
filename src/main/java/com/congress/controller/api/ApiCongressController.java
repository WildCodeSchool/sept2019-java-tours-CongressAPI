package com.congress.controller.api;

import com.congress.entity.Congress;
import com.congress.services.CongressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/congress")
public class ApiCongressController {

    @Autowired
    private CongressService service;


    @GetMapping
    List<Congress> all() {
        return service.findAll();
    }

    /**
     * This controller is used to create a congress
     *
     * @param newCongress The congress to create
     * @return Created congress
     */
    @PostMapping(consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<Congress> createCongress(@RequestPart(value = "congress", required = true) Congress newCongress,
                                                   @RequestPart(value = "logo", required = true) MultipartFile logo,
                                                   @RequestPart(value = "banner", required = true) MultipartFile banner) {
        newCongress.setLogo(logo);
        newCongress.setBanner(banner);
        return service.create(newCongress);
    }

    /**
     * This controller is used to update a congress
     *
     * @param id              The id of the updated congress
     * @param currentCongress The model of the congress
     * @return Redirect to the congress view
     */
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<Congress> updateCongress(@PathVariable long id, @RequestPart(value = "congress", required = true) Congress newCongress,
                                                   @RequestPart(value = "logo", required = true) MultipartFile logo,
                                                   @RequestPart(value = "banner", required = true) MultipartFile banner) {
        return service.update(id, logo, banner);
    }

    /**
     * This controller is used to delete a congress
     *
     * @param id The id of the congress
     * @return Redirect to the home page
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Congress> deleteCongress(@PathVariable long id) throws Exception {
        return service.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Congress> getCongress(@PathVariable long id) throws Exception {
        return service.findById(id);
    }
}
