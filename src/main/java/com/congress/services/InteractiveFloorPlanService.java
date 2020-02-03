package com.congress.services;

import com.congress.entity.InteractiveFloorPlan;
import com.congress.exception.entity.InteractiveFloorPlanNotFoundException;
import com.congress.repository.InteractiveFloorPlanRepository;
import com.congress.storage.StorageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InteractiveFloorPlanService implements CrudService<InteractiveFloorPlan> {
    private final InteractiveFloorPlanRepository interactiveFloorPlanRepository;
    private final StorageService storageService;

    public InteractiveFloorPlanService(InteractiveFloorPlanRepository interactiveFloorPlanRepository, StorageService storageService) {
        this.interactiveFloorPlanRepository = interactiveFloorPlanRepository;
        this.storageService = storageService;
    }

    public List<InteractiveFloorPlan> findByCongressId(long congressId) {
        return interactiveFloorPlanRepository.findByCongressId(congressId);
    }

    @Override
    public List<InteractiveFloorPlan> findAll() {
        return interactiveFloorPlanRepository.findAll();
    }

    @Override
    public InteractiveFloorPlan findById(long id) throws Exception {
        return interactiveFloorPlanRepository.findById(id).orElseThrow(() -> new InteractiveFloorPlanNotFoundException(id));
    }

    @Override
    public InteractiveFloorPlan create(InteractiveFloorPlan entity) {
        if (!entity.getMap().isEmpty()) {
            storageService.store(entity.getMap());
            entity.setMap_url("files/" + entity.getMap().getOriginalFilename());
        }
        return interactiveFloorPlanRepository.save(entity);
    }

    @Override
    public InteractiveFloorPlan update(long id, InteractiveFloorPlan entity) throws Exception {
        entity.setId(id);
        return this.update(entity);
    }

    @Override
    public InteractiveFloorPlan update(InteractiveFloorPlan entity) throws Exception {
        if (!interactiveFloorPlanRepository.existsById(entity.getId())) {
            throw new InteractiveFloorPlanNotFoundException(entity.getId());
        }
        if (!entity.getMap().isEmpty()) {
            storageService.store(entity.getMap());
            entity.setMap_url("files/" + entity.getMap().getOriginalFilename());
        }
        return interactiveFloorPlanRepository.save(entity);
    }

    @Override
    public void delete(long id) throws Exception {
        if (!interactiveFloorPlanRepository.existsById(id)) {
            throw new InteractiveFloorPlanNotFoundException(id);
        }
        interactiveFloorPlanRepository.deleteById(id);
    }
}
