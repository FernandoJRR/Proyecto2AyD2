package com.ayd.game_service.games.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.game_service.games.dtos.CreateGameRequestDTO;
import com.ayd.game_service.games.dtos.ScoreGameRequestDTO;
import com.ayd.game_service.games.dtos.ScoreGameResponseDTO;
import com.ayd.game_service.games.dtos.ScorePlayerRequestDTO;
import com.ayd.game_service.games.dtos.ScorePlayerResponseDTO;
import com.ayd.game_service.games.models.Game;
import com.ayd.game_service.games.repositories.GameRepository;
import com.ayd.game_service.holes.models.Hole;
import com.ayd.game_service.holes.repositories.HoleRepository;
import com.ayd.game_service.players.dtos.CreatePlayerRequestDTO;
import com.ayd.game_service.players.models.Player;
import com.ayd.game_service.players.models.PlayerHoleScore;
import com.ayd.game_service.players.repositories.PlayerHoleScoreRepository;
import com.ayd.game_service.players.repositories.PlayerRepository;
import com.ayd.shared.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @Mock
    private GameRepository gameRepository;

    @Mock
    private HoleRepository holeRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerHoleScoreRepository playerHoleScoreRepository;

    @InjectMocks
    private GameService gameService;

    private CreatePlayerRequestDTO player1Request;
    private CreatePlayerRequestDTO player2Request;
    private CreateGameRequestDTO creationRequest;

    private Game foundGame;
    private Game playGame;
    private Hole playHole;

    private Player playingPlayer;

    private Player player1;
    private Player player2;

    private ScorePlayerRequestDTO scoreRequest;
    private ScoreGameRequestDTO scoreGameRequest;

    private ScorePlayerRequestDTO finalScoreRequest;
    private ScoreGameRequestDTO finalHoleRequest;

    private PlayerHoleScore score1;
    private PlayerHoleScore score2;
    private PlayerHoleScore score3;

    private List<PlayerHoleScore> scoresList;

    private String PLAYER_1_NAME = "Jorge";
    private String PLAYER_1_ID = "kfsd-cwmd-kmsc-kdss";
    private String PLAYER_2_NAME = "Maria";
    private String PLAYER_2_ID = "kfsd-cwmd-dsms-mksd";

    private String PLAYER_ID = "cksd-sdkl-kocd-csdo";

    private String PACKAGE_ID = "jofd-mkfd-fdss-cdsm";
    private String RESERVATION_ID = "fsdl-sfdl-fdsl-flms";
    private String GAME_ID = "peww-csdm-sdfl-lmkc";
    private String INVALID_GAME_ID = "fdks-cdmk-fdsm-mdsc";
    private String INVALID_RESERVATION_ID = "fdks-pefw-mdfs-mdsf";
    private Integer HOLE_NUMBER = 5;
    private Integer LAST_HOLE_NUMBER = 18;
    private Integer CURRENT_HOLE_NUMBER = 4;
    private Integer PLAYER_SHOTS = 3;

    @BeforeEach
    public void setUp() {
        player1Request = new CreatePlayerRequestDTO();
        player1Request.setName(PLAYER_1_NAME);
        player1Request.setPlayerNumber(1);

        player2Request = new CreatePlayerRequestDTO();
        player2Request.setName(PLAYER_2_NAME);
        player2Request.setPlayerNumber(2);

        creationRequest = new CreateGameRequestDTO();
        creationRequest.setReservationId(RESERVATION_ID);
        creationRequest.setPlayers(Arrays.asList(player1Request, player2Request));

        foundGame = new Game();
        foundGame.setId(GAME_ID);
        foundGame.setReservationId(RESERVATION_ID);


        playGame = new Game();
        playGame.setReservationId(GAME_ID);
        playGame.setCurrentHole(CURRENT_HOLE_NUMBER);
        playGame.setHasFinished(false);

        playHole = new Hole();
        playHole.setNumber(HOLE_NUMBER);

        playingPlayer = new Player();
        playingPlayer.setId(PLAYER_ID);

        scoreRequest = new ScorePlayerRequestDTO();
        scoreRequest.setPlayerId(PLAYER_ID);
        scoreRequest.setShotsPlayer(PLAYER_SHOTS);

        scoreGameRequest = new ScoreGameRequestDTO();
        scoreGameRequest.setHoleNumber(HOLE_NUMBER);
        scoreGameRequest.setScorePlayers(List.of(scoreRequest));


        finalHoleRequest = new ScoreGameRequestDTO();
        finalHoleRequest.setHoleNumber(LAST_HOLE_NUMBER);

        finalScoreRequest = new ScorePlayerRequestDTO();
        finalScoreRequest.setPlayerId(PLAYER_ID);
        finalScoreRequest.setShotsPlayer(2);

        finalHoleRequest.setScorePlayers(List.of(finalScoreRequest));

        player1 = new Player();
        player1.setId(PLAYER_1_ID);
        player1.setName(PLAYER_1_NAME);

        player2 = new Player();
        player2.setId(PLAYER_2_ID);
        player2.setName(PLAYER_2_NAME);

        score1 = new PlayerHoleScore();
        score1.setPlayer(player1);
        score1.setShots(3);

        score2 = new PlayerHoleScore();
        score2.setPlayer(player1);
        score2.setShots(4);

        score3 = new PlayerHoleScore();
        score3.setPlayer(player2);
        score3.setShots(2);

        scoresList = Arrays.asList(score1, score2, score3);
    }

    @Test
    void createGame_Success() {
            // ARRANGE
            when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

            ArgumentCaptor<Game> gameCaptor = ArgumentCaptor.forClass(Game.class);

            // ACT
            Game savedGame = gameService.createGame(creationRequest);

            // ASSERT
            verify(gameRepository).save(gameCaptor.capture());
            Game capturedGame = gameCaptor.getValue();
            List<Player> players = capturedGame.getPlayers();

            assertAll(
                () -> assertEquals(RESERVATION_ID, capturedGame.getReservationId()),
                () -> assertFalse(capturedGame.getHasFinished()),
                () -> assertNotNull(capturedGame.getPlayers()),
                () -> assertEquals(2, capturedGame.getPlayers().size()),
                () -> assertEquals(PLAYER_1_NAME, players.get(0).getName()),
                () -> assertEquals(PLAYER_2_NAME, players.get(1).getName()),
                () -> assertSame(capturedGame, players.get(0).getGame()),
                () -> assertSame(capturedGame, players.get(1).getGame()),
                () -> assertEquals(savedGame, capturedGame)
            );

    }

    @Test
    void getGameById_Success() throws NotFoundException {
        // ARRANGE
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(foundGame));

        // ACT
        Game result = gameService.getGameById(GAME_ID);

        // ASSERT
        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(GAME_ID, result.getId())
        );

        verify(gameRepository).findById(GAME_ID);
    }

    @Test
    void getGameById_NotFound() {
        // ARRANGE
        when(gameRepository.findById(INVALID_GAME_ID)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(NotFoundException.class, () -> {
            gameService.getGameById(INVALID_GAME_ID);
        });
    }

    @Test
    void getGameByReservationId_Success() throws NotFoundException {
        // ARRANGE
        when(gameRepository.findByReservationId(RESERVATION_ID)).thenReturn(Optional.of(foundGame));

        // ACT
        Game result = gameService.getGameByReservationId(RESERVATION_ID);

        // ASSERT
        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(RESERVATION_ID, result.getReservationId())
        );
        verify(gameRepository).findByReservationId(RESERVATION_ID);
    }

    @Test
    void getGameByReservationId_NotFound() {
        // ARRANGE
        when(gameRepository.findByReservationId(INVALID_RESERVATION_ID)).thenReturn(Optional.empty());

        // ACT
        // ASSERT
        assertThrows(NotFoundException.class, () -> {
            gameService.getGameByReservationId(INVALID_RESERVATION_ID);
        });
    }

    @Test
    void updateScore_Success() throws NotFoundException {
        // ARRANGE
        when(gameRepository.findByReservationId(GAME_ID)).thenReturn(Optional.of(playGame));
        when(holeRepository.findByNumber(HOLE_NUMBER)).thenReturn(Optional.of(playHole));
        when(playerRepository.findById(PLAYER_ID)).thenReturn(Optional.of(playingPlayer));
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        Game result = gameService.updateScore(GAME_ID, scoreGameRequest);

        // ASSERT
        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(HOLE_NUMBER, result.getCurrentHole()),
            () -> assertFalse(result.getHasFinished())
        );
        verify(playerHoleScoreRepository).save(any(PlayerHoleScore.class));
        verify(gameRepository).save(result);
    }

    @Test
    void updateScore_SuccessLastHole() throws NotFoundException {
        // ARRANGE
        when(gameRepository.findByReservationId(GAME_ID)).thenReturn(Optional.of(playGame));
        when(holeRepository.findByNumber(LAST_HOLE_NUMBER)).thenReturn(Optional.of(new Hole()));
        when(playerRepository.findById(PLAYER_ID)).thenReturn(Optional.of(playingPlayer));
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        Game result = gameService.updateScore(GAME_ID, finalHoleRequest);

        // ASSERT
        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(LAST_HOLE_NUMBER, result.getCurrentHole()),
            () -> assertEquals(true, result.getHasFinished())
        );
        verify(playerHoleScoreRepository).save(any(PlayerHoleScore.class));
    }

    @Test
    void updateScore_GameNotFound_ThrowsNotFoundException() {
        // ARRANGE
        when(gameRepository.findByReservationId(INVALID_GAME_ID)).thenReturn(Optional.empty());

        // ACT
        // ASSERT
        assertThrows(NotFoundException.class, () ->
            gameService.updateScore(INVALID_GAME_ID, scoreGameRequest)
        );
    }

    @Test
    void getScore_WithValidScores_ReturnsAggregatedResult() throws NotFoundException {
        when(playerHoleScoreRepository.findByGame_Id(GAME_ID)).thenReturn(scoresList);

        // ACT
        ScoreGameResponseDTO response = gameService.getScore(GAME_ID);

        // ASSERT
        assertNotNull(response);
        assertEquals(2, response.getPlayerScores().size());

        ScorePlayerResponseDTO player1Score = response.getPlayerScores().stream()
            .filter(p -> p.getId().equals(PLAYER_1_ID))
            .findFirst().orElse(null);

        ScorePlayerResponseDTO player2Score = response.getPlayerScores().stream()
            .filter(p -> p.getId().equals(PLAYER_2_ID))
            .findFirst().orElse(null);

        assertNotNull(player1Score);
        assertEquals(PLAYER_1_NAME, player1Score.getName());
        assertEquals(7, player1Score.getTotalShots());

        assertNotNull(player2Score);
        assertEquals(PLAYER_2_NAME, player2Score.getName());
        assertEquals(2, player2Score.getTotalShots());

        verify(playerHoleScoreRepository).findByGame_Id(GAME_ID);
    }

    @Test
    void getScore_NoScores() throws NotFoundException {
        // ARRANGE
        when(playerHoleScoreRepository.findByGame_Id(GAME_ID)).thenReturn(List.of());

        // ACT
        ScoreGameResponseDTO response = gameService.getScore(GAME_ID);

        // ASSERT
        assertNotNull(response);
        assertNotNull(response.getPlayerScores());
        assertTrue(response.getPlayerScores().isEmpty());

        verify(playerHoleScoreRepository).findByGame_Id(GAME_ID);
    }
}
