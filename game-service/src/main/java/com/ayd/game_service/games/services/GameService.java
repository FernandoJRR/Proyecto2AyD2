package com.ayd.game_service.games.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ayd.game_service_common.games.dtos.CreateGameRequestDTO;
import com.ayd.game_service.games.dtos.PlayerNameRequestDTO;
import com.ayd.game_service.games.dtos.ScoreGameRequestDTO;
import com.ayd.game_service.games.dtos.ScoreGameResponseDTO;
import com.ayd.game_service.games.dtos.ScorePlayerRequestDTO;
import com.ayd.game_service.games.dtos.ScorePlayerResponseDTO;
import com.ayd.game_service.games.dtos.UpdatePlayersRequestDTO;
import com.ayd.game_service.games.models.Game;
import com.ayd.game_service.games.ports.ForGamesPort;
import com.ayd.game_service.games.repositories.GameRepository;
import com.ayd.game_service.holes.models.Hole;
import com.ayd.game_service.holes.repositories.HoleRepository;
import com.ayd.game_service_common.players.dtos.CreatePlayerRequestDTO;
import com.ayd.game_service.players.models.Player;
import com.ayd.game_service.players.models.PlayerHoleScore;
import com.ayd.game_service.players.repositories.PlayerHoleScoreRepository;
import com.ayd.game_service.players.repositories.PlayerRepository;
import com.ayd.shared.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class GameService implements ForGamesPort {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final PlayerHoleScoreRepository playerHoleScoreRepository;
    private final HoleRepository holeRepository;

    public Game createGame(CreateGameRequestDTO request) {
        String reservationId = request.getReservationId();
        List<CreatePlayerRequestDTO> playersRequest = request.getPlayers();

        Game game = new Game();
        game.setReservationId(reservationId);
        game.setHasFinished(false);

        List<Player> createdPlayers = new ArrayList<>();

        for (CreatePlayerRequestDTO playerDTO : playersRequest) {
            Player createPlayer = new Player();
            createPlayer.setName(playerDTO.getName());
            createPlayer.setPlayerNumber(playerDTO.getPlayerNumber());
            createPlayer.setGame(game);

            createdPlayers.add(createPlayer);
        }

        game.setPlayers(createdPlayers);

        return gameRepository.save(game);
    }

    public List<Game> getGames() {
        return gameRepository.findAll();
    }

    public Game getGameById(String gameId) throws NotFoundException {
        return gameRepository.findById(gameId)
            .orElseThrow(() -> new NotFoundException("No se ha encontrado el juego con ese id"));
    }

    public Game getGameByReservationId(String reservationId) throws NotFoundException {
        return gameRepository.findByReservationId(reservationId)
            .orElseThrow(() -> new NotFoundException("No se ha encontrado el juego con ese id de reservacion"));
    }

    public Game updateScore(String gameId, ScoreGameRequestDTO request) throws NotFoundException, IllegalArgumentException {
        Game currentGame = gameRepository.findById(gameId)
            .orElseThrow(() -> new NotFoundException("No se ha encontrado el juego con ese id"));

        Hole currentHole = holeRepository.findByNumber(request.getHoleNumber())
            .orElseThrow(() -> new NotFoundException("Hoyo no encontrado"));

        Integer scoreHoleNumber = request.getHoleNumber();
        List<ScorePlayerRequestDTO> scorePlayerRequest = request.getScorePlayers();

        if (currentGame.getCurrentHole() != null && currentGame.getCurrentHole() >= scoreHoleNumber) {
            throw new IllegalArgumentException("El hoyo ingresado es menor al progreso actual");
        }
        currentGame.setCurrentHole(scoreHoleNumber);

        //Si este hoyo es el ultimo el juego es completado
        if (scoreHoleNumber == 18) {
            currentGame.setHasFinished(true);
        }

        //Se da el punteo para cada jugador
        for (ScorePlayerRequestDTO playerScore : scorePlayerRequest) {
            Player currentPlayer = playerRepository.findById(playerScore.getPlayerId())
                .orElseThrow(() -> new NotFoundException("El jugador no fue encontrado"));

            PlayerHoleScore score = new PlayerHoleScore();
            score.setPlayer(currentPlayer);
            score.setHole(currentHole);
            score.setGame(currentGame);
            score.setShots(playerScore.getShotsPlayer());

            playerHoleScoreRepository.save(score);
        }


        return gameRepository.save(currentGame);
    }

    public ScoreGameResponseDTO getScore(String gameId) throws NotFoundException {
        List<PlayerHoleScore> scores = playerHoleScoreRepository.findByGame_Id(gameId);

        Map<String, List<PlayerHoleScore>> groupedScores = scores.stream()
            .collect(Collectors.groupingBy(s -> s.getPlayer().getId()));

            List<ScorePlayerResponseDTO> playerScoresList = groupedScores.entrySet().stream()
            .map(entry -> {
                String playerId = entry.getKey();
                List<PlayerHoleScore> playerScores = entry.getValue();

                String playerName = playerScores.get(0).getPlayer().getName();
                int totalShots = playerScores.stream()
                    .mapToInt(PlayerHoleScore::getShots)
                    .sum();

                ScorePlayerResponseDTO dto = new ScorePlayerResponseDTO();
                dto.setId(playerId);
                dto.setName(playerName);
                dto.setTotalShots(totalShots);

                return dto;
            })
            .collect(Collectors.toList());

            ScoreGameResponseDTO response = new ScoreGameResponseDTO();
            response.setPlayerScores(playerScoresList);
            return response;
    }

    public ScoreGameResponseDTO getScoreHole(String gameId, Integer holeNumber) throws NotFoundException {
        List<PlayerHoleScore> scores = playerHoleScoreRepository.findByGame_IdAndHole_Number(gameId, holeNumber);

        Map<String, List<PlayerHoleScore>> groupedScores = scores.stream()
            .collect(Collectors.groupingBy(s -> s.getPlayer().getId()));

            List<ScorePlayerResponseDTO> playerScoresList = groupedScores.entrySet().stream()
            .map(entry -> {
                String playerId = entry.getKey();
                List<PlayerHoleScore> playerScores = entry.getValue();

                String playerName = playerScores.get(0).getPlayer().getName();
                int totalShots = playerScores.stream()
                    .mapToInt(PlayerHoleScore::getShots)
                    .sum();

                ScorePlayerResponseDTO dto = new ScorePlayerResponseDTO();
                dto.setId(playerId);
                dto.setName(playerName);
                dto.setTotalShots(totalShots);

                return dto;
            })
            .collect(Collectors.toList());

            ScoreGameResponseDTO response = new ScoreGameResponseDTO();
            response.setPlayerScores(playerScoresList);
            return response;
    }

    public Game updatePlayersGame(String gameId, UpdatePlayersRequestDTO request) throws NotFoundException {
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new NotFoundException("Juego no encontrado"));

        if (game.getPlayers() == null) {
            game.setPlayers(new ArrayList<>());
        } else {
            game.getPlayers().clear();
        }

        for (PlayerNameRequestDTO playerDto : request.getPlayers()) {
            Player player = new Player();
            player.setName(playerDto.getName());
            player.setPlayerNumber(playerDto.getPlayerNumber());
            player.setGame(game);
            game.getPlayers().add(player);
        }

        return gameRepository.save(game);
    }
}
