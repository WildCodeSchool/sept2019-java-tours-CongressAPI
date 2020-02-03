package com.congress.services;

import com.congress.entity.FloorPlan;
import com.congress.exception.FloorPlanNotFoundException;
import com.congress.repository.FloorPlanRepository;
import com.congress.storage.StorageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorPlanService implements CrudService<FloorPlan> {
    private final FloorPlanRepository floorPlanRepository;
    private final StorageService storageService;

    public FloorPlanService(FloorPlanRepository floorPlanRepository, StorageService storageService) {
        this.floorPlanRepository = floorPlanRepository;
        this.storageService = storageService;
    }

    @Override
    public List<FloorPlan> findAll() {
        return floorPlanRepository.findAll();
    }

    public List<FloorPlan> findByCongressId(long congressId) {
        return floorPlanRepository.findByCongressId(congressId);

    }

    @Override
    public FloorPlan findById(long id) throws Exception {
        return floorPlanRepository.findById(id).orElseThrow(() -> new FloorPlanNotFoundException(id));
    }

    @Override
    public FloorPlan create(FloorPlan entity) {
        if (!entity.getPlan().isEmpty()) {
            storageService.store(entity.getPlan());
            entity.setPlan_url("/files/" + entity.getPlan().getOriginalFilename());
        }
        return floorPlanRepository.save(entity);
    }

    @Override
    public FloorPlan update(long id, FloorPlan entity) throws Exception {
        if (!floorPlanRepository.existsById(id)) {
            throw new FloorPlanNotFoundException(id);
        }
        entity.setId(id);
        if (!entity.getPlan().isEmpty()) {
            storageService.store(entity.getPlan());
            entity.setPlan_url("/files/" + entity.getPlan().getOriginalFilename());
        }
        return floorPlanRepository.save(entity);
    }

    @Override
    public FloorPlan update(FloorPlan entity) throws Exception {
        if (!floorPlanRepository.existsById(entity.getId())) {
            throw new FloorPlanNotFoundException(entity.getId());
        }
        if (!entity.getPlan().isEmpty()) {
            storageService.store(entity.getPlan());
            entity.setPlan_url("/files/" + entity.getPlan().getOriginalFilename());
        }
        return floorPlanRepository.save(entity);
    }

    @Override
    public void delete(long id) throws Exception {
        if (!floorPlanRepository.existsById(id)) {
            throw new FloorPlanNotFoundException(id);
        }
        floorPlanRepository.deleteById(id);
    }
}