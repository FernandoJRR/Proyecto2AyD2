package com.ayd.game_service.players.models;

import org.hibernate.annotations.DynamicUpdate;

import com.ayd.game_service.games.models.Game;
import com.ayd.game_service.holes.models.Hole;
import com.ayd.shared.models.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class PlayerHoleScore extends Auditor {
    @ManyToOne
    private Player player;

    @ManyToOne
    private Hole hole;

    @ManyToOne
    private Game game;

    @Column(nullable = false)
    private Integer shots;
}
