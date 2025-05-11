package com.ayd.game_service.holes.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd.game_service.holes.models.Hole;

public interface HoleRepository extends JpaRepository<Hole, String> {
    Optional<Hole> findByNumber(Integer number);
}
