package com.congress.controller.api;

import com.congress.entity.Hotel;
import com.congress.services.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/hotel")
public class ApiHotelController {

    private final HotelService service;

    public ApiHotelController(HotelService hotelService) {
        this.service = hotelService;
    }

    @GetMapping
    List<Hotel> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotel(@PathVariable long id) throws Exception {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping(consumes = {"multipart/from-data"})
    @Valid
    public ResponseEntity<Hotel> createHotel(Hotel hotel) {
        return ResponseEntity.ok(service.create(hotel));
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<Hotel> updateHotel(Hotel hotel) throws Exception {
        return ResponseEntity.ok(service.update(hotel));
    }

    @DeleteMapping("/{id}")
    public void deleteHotel(@PathVariable long id) throws Exception {
        service.delete(id);
    }

}
