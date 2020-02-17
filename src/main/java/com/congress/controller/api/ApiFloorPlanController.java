package com.congress.controller.api;

import com.congress.entity.Congress;
import com.congress.entity.FloorPlan;
import com.congress.services.CongressService;
import com.congress.services.FloorPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/congress/{congressId}/floorPlan")
public class ApiFloorPlanController {
    private final FloorPlanService service;
    private final CongressService congressService;

    public ApiFloorPlanController(FloorPlanService service, CongressService congressService) {
        this.service = service;
        this.congressService = congressService;
    }

    @GetMapping
    List<FloorPlan> all(@PathVariable long congressId) {
        return service.findByCongressId(congressId);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<FloorPlan> createSponsor(@PathVariable long congressId, FloorPlan savedFloorPlan) throws IOException {
        Congress currentCongress = congressService.findById(congressId);
        currentCongress.addFloorPlan(savedFloorPlan);
        savedFloorPlan = service.create(savedFloorPlan);
        return ResponseEntity.ok(savedFloorPlan);
    }

    @PutMapping(value = "{/id}", consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<FloorPlan> updateFloorPlan(@PathVariable long congressId, FloorPlan updatedFloorPlan) throws Exception {
        Congress currentCongress = congressService.findById(congressId);
        currentCongress.addFloorPlan(updatedFloorPlan);
        updatedFloorPlan = service.update(updatedFloorPlan);
        return ResponseEntity.ok(updatedFloorPlan);
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
