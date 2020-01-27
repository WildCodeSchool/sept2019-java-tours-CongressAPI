package com.congress.repository;

import com.congress.entity.Congress;
import com.congress.entity.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
    List<Congress> findByCongressId(long congress_id);
}
