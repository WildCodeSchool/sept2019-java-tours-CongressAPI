package com.congress.controller.api;

import com.congress.entity.Congress;
import com.congress.entity.Map;
import com.congress.services.CongressService;
import com.congress.services.MapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/congress/{congressId}/map")
public class ApiMapController {

    private final MapService service;
    private final CongressService congressService;

    public ApiMapController(MapService service, CongressService congressService) {
        this.service = service;
        this.congressService = congressService;
    }

    @GetMapping
    List<Map> all(@PathVariable long congressId) {
        return service.findByCongressId(congressId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map> getMap(@PathVariable long id) throws Exception {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping(consumes = {"multipart/from-data"})
    @Valid
    public ResponseEntity<Map> createMap(@PathVariable long congressId, Map savedMap) throws Exception {
        Congress currentCongress = congressService.findById(congressId);
        currentCongress.addMap(savedMap);
        savedMap = service.create(savedMap);
        return ResponseEntity.ok(savedMap);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<Map> updateHotel(@PathVariable long congressId, Map updatedMap) throws Exception {
        Congress currentCongress = congressService.findById(congressId);
        currentCongress.addMap(updatedMap);
        updatedMap = service.update(updatedMap);
        return ResponseEntity.ok(updatedMap);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @Valid
    public void deleteMap(@PathVariable long id) throws Exception {
        service.delete(id);
    }

}
