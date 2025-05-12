package com.ayd.game_service.games.ports;

import com.ayd.game_service.games.dtos.CreateGameRequestDTO;
import com.ayd.game_service.games.dtos.ScoreGameRequestDTO;
import com.ayd.game_service.games.models.Game;
import com.ayd.shared.exceptions.IllegalArgumentException;
import com.ayd.shared.exceptions.NotFoundException;

public interface ForGamesPort {
    public Game createGame(CreateGameRequestDTO request);
    public Game getGameById(String gameId) throws NotFoundException;
    public Game getGameByReservationId(String reservationId) throws NotFoundException;
    public Game scoreGame(String gameId, ScoreGameRequestDTO request) throws NotFoundException, IllegalArgumentException;
}
