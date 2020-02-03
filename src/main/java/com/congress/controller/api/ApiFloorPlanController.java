package com.congress.controller.api;

import com.congress.entity.FloorPlan;
import com.congress.services.FloorPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/congress/{congressId}/floorPlan")
public class ApiFloorPlanController {
    private final FloorPlanService service;

    public ApiFloorPlanController(FloorPlanService service) {
        this.service = service;
    }

    @GetMapping
    List<FloorPlan> all(@PathVariable long congressId) {
        return service.findByCongressId(congressId);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<FloorPlan> createSponsor(FloorPlan floorPlan) {
        FloorPlan savedCongress = service.create(floorPlan);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCongress.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "{/id}", consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<FloorPlan> updateFloorPlan(FloorPlan floorPlan) throws Exception {
        return ResponseEntity.ok(service.update(floorPlan));
    }

    @DeleteMapping("/{id}")
    public void deleteFloorPlan(@PathVariable long id) throws Exception {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FloorPlan> getFloorPlan(@PathVariable long id) throws Exception {
        return ResponseEntity.ok(service.findById(id));
    }
}
