package com.congress.repository;

import com.congress.entity.Map;
import org.springframework.data.jpa.repository.JpaRepository;

// voila
public interface MapRepository extends JpaRepository<Map, Long> {
}
