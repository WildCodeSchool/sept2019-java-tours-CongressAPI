package com.congress.services;

import com.congress.entity.Map;
import com.congress.exception.entity.MapNotFoundException;
import com.congress.repository.MapRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapService implements CrudService<Map> {

    private final MapRepository mapRepository;

    public MapService(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    @Override
    public List<Map> findAll() {
        return mapRepository.findAll();
    }

    public List<Map> findByCongressId(long congressId) {
        return mapRepository.findByCongressId(congressId);
    }

    @Override
    public Map findById(long id) throws Exception {
        return mapRepository.findById(id).orElseThrow(() -> new MapNotFoundException(id));
    }

    @Override
    public Map create(Map entity) {
        return mapRepository.save(entity);
    }

    @Override
    public Map update(long id, Map entity) throws Exception {
        entity.setId(id);
        return this.update(entity);
    }

    @Override
    public Map update(Map entity) throws Exception {
        if (!mapRepository.existsById(entity.getId())) {
            throw new MapNotFoundException(entity.getId());
        }
        return mapRepository.save(entity);
    }

    @Override
    public void delete(long id) throws Exception {
        if (!mapRepository.existsById(id)) {
            throw new MapNotFoundException(id);
        }
    }
}
