package com.ayd.game_service.games.mappers;

import org.mapstruct.Mapper;

import com.ayd.game_service.games.dtos.GameResponseDTO;
import com.ayd.game_service.games.models.Game;
import com.ayd.game_service.players.mappers.PlayerMapper;

@Mapper(componentModel = "spring", uses = PlayerMapper.class)
public interface GamesMapper {
    public GameResponseDTO fromGameToResponseDTO(Game game);
}
