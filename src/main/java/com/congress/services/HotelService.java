package com.congress.services;

import com.congress.entity.Congress;
import com.congress.entity.Hotel;
import com.congress.exception.HotelNotFoundException;
import com.congress.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService implements CrudService<Hotel> {
    private final HotelRepository hotelRepository;
    private final CongressService congressService;

    public HotelService(HotelRepository hotelRepository, CongressService congressService) {
        this.hotelRepository = hotelRepository;
        this.congressService = congressService;
    }

    @Override
    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel findById(long id) throws Exception {
        return hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException(id));
    }

    @Override
    public Hotel create(Hotel entity) {
        return hotelRepository.save(entity);
    }

    @Override
    public Hotel update(long id, Hotel entity) throws Exception {
        if (!hotelRepository.existsById(id)) {
            throw new HotelNotFoundException(id);
        }
        entity.setId(id);
        return hotelRepository.save(entity);
    }

    @Override
    public Hotel update(Hotel entity) throws Exception {
        if (!hotelRepository.existsById(entity.getId())) {
            throw new HotelNotFoundException(entity.getId());
        }
        return hotelRepository.save(entity);
    }

    @Override
    public void delete(long id) throws Exception {
        if (!hotelRepository.existsById(id)) {
            throw new HotelNotFoundException(id);
        }
        hotelRepository.deleteById(id);
    }

    public void linkToCongress(Long congressId, long id) throws Exception {
        Congress congress = congressService.findById(congressId);
        Hotel sponsor = this.findById(id);
        sponsor.addCongress(congress);
        this.update(sponsor);
        congressService.update(congress);
    }

    public void unLinkToCongress(Long congressId, long id) throws Exception {
        Congress congress = congressService.findById(congressId);
        Hotel sponsor = this.findById(id);
        sponsor.removeCongress(congress);
        this.update(sponsor);
        congressService.update(congress);
    }

}
