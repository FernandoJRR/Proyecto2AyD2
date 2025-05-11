package com.ayd.game_service.players.mappers;

import org.mapstruct.Mapper;

import com.ayd.game_service.players.dtos.PlayerResponseDTO;
import com.ayd.game_service.players.models.Player;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    public PlayerResponseDTO fromPlayerToResponseDTO(Player player);
}
