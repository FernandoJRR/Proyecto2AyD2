package com.ayd.game_service.games.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd.game_service.games.models.Game;

public interface GameRepository extends JpaRepository<Game, String> {
    Optional<Game> findByReservationId(String reservationId);
}
