package com.congress.repository;

import com.congress.entity.Congress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CongressRepository extends JpaRepository<Congress, Long> {

}
