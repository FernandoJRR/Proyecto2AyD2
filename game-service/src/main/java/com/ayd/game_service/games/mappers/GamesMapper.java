package com.ayd.game_service.games.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.game_service.games.models.Game;
import com.ayd.game_service.players.mappers.PlayerMapper;
import com.ayd.game_service_common.games.dtos.GameResponseDTO;

@Mapper(componentModel = "spring", uses = PlayerMapper.class)
public interface GamesMapper {
    public GameResponseDTO fromGameToResponseDTO(Game game);
    public List<GameResponseDTO> fromGamesToResponseDTO(List<Game> games);
}
