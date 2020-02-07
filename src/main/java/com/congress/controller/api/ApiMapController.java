package com.congress.controller.api;

import com.congress.entity.Congress;
import com.congress.entity.Map;
import com.congress.services.CongressService;
import com.congress.services.MapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/map")
public class ApiMapController {

    private final MapService serviceMap;
    private final CongressService congressService;

    public ApiMapController(MapService service, CongressService congressService){
        this.serviceMap = service;
        this.congressService = congressService;
    }
    @GetMapping
    List<Map> all(@PathVariable long congressId){
        return serviceMap.findByCongressId(congressId);
    }
    @PostMapping(consumes = {"multipart/from-data"})
    @Valid
    public ResponseEntity<Map> createMap(@PathVariable long congressId, Map map) throws Exception{
        Congress currentCongress = congressService.findById(congressId);
        currentCongress.addMap(map);
        Map savedCongress = serviceMap.create(map);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand((savedCongress).getId()).toUri();
        return ResponseEntity.created(location).build();
    }
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @Valid
    public void deleteMap(@PathVariable long id) throws Exception{
        serviceMap.delete(id);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Map> getMap(@PathVariable long id) throws Exception {
        return ResponseEntity.ok(serviceMap.findById(id));
    }
}
