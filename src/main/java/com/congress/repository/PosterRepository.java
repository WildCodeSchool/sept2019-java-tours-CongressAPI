package com.congress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.congress.entity.Poster;

@Repository
public interface PosterRepository extends JpaRepository<Poster, Long> {

}
