package com.ayd.game_service.players.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd.game_service.players.models.Player;

public interface PlayerRepository extends JpaRepository<Player, String> {
}
