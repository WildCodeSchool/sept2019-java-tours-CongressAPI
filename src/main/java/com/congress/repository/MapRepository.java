package com.congress.repository;

import com.congress.entity.Map;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// voila
public interface MapRepository extends JpaRepository<Map, Long> {
    List<Map> findByCongressId(long congress_id);
}
