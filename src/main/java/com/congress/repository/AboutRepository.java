package com.congress.repository;

import com.congress.entity.About;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AboutRepository extends JpaRepository<About, Long> {
    List<About> findAllByCongressId(long congress_id);
}
