package com.ayd.game_service.players.models;

import org.hibernate.annotations.DynamicUpdate;

import com.ayd.game_service.games.models.Game;
import com.ayd.shared.models.Auditor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class Player extends Auditor {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer playerNumber;
    @ManyToOne
    private Game game;
}
