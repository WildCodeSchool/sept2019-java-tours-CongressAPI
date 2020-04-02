package com.congress.repository;

import com.congress.entity.Activity;
import com.congress.entity.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByCongressId(long congress_id);

}
