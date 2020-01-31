package com.congress.repository;

import com.congress.entity.Congress;
import com.congress.entity.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
    List<Congress> findByCongressId(long congress_id);
}
