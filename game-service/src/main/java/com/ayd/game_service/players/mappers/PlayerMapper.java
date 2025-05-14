package com.ayd.game_service.players.mappers;

import org.mapstruct.Mapper;

import com.ayd.game_service.players.models.Player;
import com.ayd.game_service_common.players.dtos.PlayerResponseDTO;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    public PlayerResponseDTO fromPlayerToResponseDTO(Player player);
}
