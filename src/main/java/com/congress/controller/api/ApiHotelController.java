package com.congress.controller.api;

import com.congress.entity.Hotel;
import com.congress.services.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/hotel")
public class ApiHotelController {

    private final HotelService hotelService;

    public ApiHotelController(HotelService hotelService){
        this.hotelService = hotelService;
    }

    @GetMapping
    List<Hotel> all(){
        return hotelService.findAll();
    }
    @PostMapping(consumes = {"multipart/from-data"})
    @Valid
    public ResponseEntity<Hotel> createHotel(Hotel hotel){
        Hotel savedCongress = hotelService.create(hotel);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCongress.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @Valid
    public ResponseEntity<Hotel> updateHotel(Hotel hotel)throws Exception{
        return ResponseEntity.ok(hotelService.update(hotel));
    }

    @DeleteMapping("/{id}")
    public void deleteHotel(@PathVariable long id) throws Exception {
        hotelService.delete(id);
    }@GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotel(@PathVariable long id) throws Exception {
        return ResponseEntity.ok(hotelService.findById(id));
    }

}
