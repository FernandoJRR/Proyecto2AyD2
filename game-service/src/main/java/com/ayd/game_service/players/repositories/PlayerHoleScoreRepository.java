package com.ayd.game_service.players.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd.game_service.players.models.PlayerHoleScore;

public interface PlayerHoleScoreRepository extends JpaRepository<PlayerHoleScore, String> {
    List<PlayerHoleScore> findByGame_Id(String gameId);
}
