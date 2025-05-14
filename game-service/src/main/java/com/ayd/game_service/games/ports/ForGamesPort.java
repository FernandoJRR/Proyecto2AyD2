package com.ayd.game_service.games.ports;

import java.util.List;

import com.ayd.game_service.games.dtos.CreateGameRequestDTO;
import com.ayd.game_service.games.dtos.ScoreGameRequestDTO;
import com.ayd.game_service.games.dtos.ScoreGameResponseDTO;
import com.ayd.game_service.games.models.Game;
import com.ayd.shared.exceptions.NotFoundException;

public interface ForGamesPort {
    public List<Game> getGames() throws NotFoundException;
    public Game createGame(CreateGameRequestDTO request);
    public Game getGameById(String gameId) throws NotFoundException;
    public Game getGameByReservationId(String reservationId) throws NotFoundException;
    public Game updateScore(String gameId, ScoreGameRequestDTO request) throws NotFoundException, IllegalArgumentException;
    public ScoreGameResponseDTO getScore(String gameId) throws NotFoundException;
}
