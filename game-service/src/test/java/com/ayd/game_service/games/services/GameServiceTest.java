package com.ayd.game_service.games.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.game_service.games.dtos.CreateGameRequestDTO;
import com.ayd.game_service.games.models.Game;
import com.ayd.game_service.players.dtos.CreatePlayerRequestDTO;
import com.ayd.game_service.players.models.Player;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @BeforeEach
    public void setUp() {
    }

    @Test
    void createGame_Success() {
    }
}
