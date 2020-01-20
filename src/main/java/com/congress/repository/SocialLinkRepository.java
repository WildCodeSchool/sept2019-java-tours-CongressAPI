package com.congress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.congress.entity.SocialLink;

@Repository
public interface SocialLinkRepository extends JpaRepository<SocialLink, Long> {

}
