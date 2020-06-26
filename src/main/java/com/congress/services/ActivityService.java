package com.congress.services;

import com.congress.entity.Activity;
import com.congress.exception.ActivityNotFoundException;
import com.congress.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService implements CrudService<Activity> {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    @Override
    public Activity findById(long id) {
        return activityRepository.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));
    }

    @Override
    public Activity create(Activity entity) {
        return activityRepository.save(entity);
    }

    @Override
    public Activity update(long id, Activity entity) throws Exception {
        if (!activityRepository.existsById(id)) {
            throw new ActivityNotFoundException(id);
        }
        entity.setId(id);
        return activityRepository.save(entity);
    }

    @Override
    public Activity update(Activity entity) throws Exception {
        if (!activityRepository.existsById(entity.getId())) {
            throw new ActivityNotFoundException(entity.getId());
        }
        return null;
    }

    @Override
    public void delete(long id) throws Exception {
        if (!activityRepository.existsById(id)) {
            throw new ActivityNotFoundException(id);
        }
    }
}
