package com.bforbank.tennis_api.infrastructure.adapter.repository.springdata;

import com.bforbank.tennis_api.infrastructure.adapter.repository.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataMatchRepository extends JpaRepository<MatchEntity, String> {
}
