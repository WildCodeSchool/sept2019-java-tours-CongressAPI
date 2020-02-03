package com.congress.repository;

import com.congress.entity.InteractiveFloorPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InteractiveFloorPlanRepository extends JpaRepository<InteractiveFloorPlan, Long> {
    List<InteractiveFloorPlan> findByCongressId(long congress_id);
}
