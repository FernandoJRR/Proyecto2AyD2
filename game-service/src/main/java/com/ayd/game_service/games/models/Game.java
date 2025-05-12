package com.ayd.game_service.games.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import com.ayd.game_service.players.models.Player;
import com.ayd.shared.models.Auditor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class Game extends Auditor {
    @Column(nullable = false)
    private String packageId;

    @Column(nullable = false)
    private String reservationId;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players;

    @Column(nullable = false)
    private boolean hasFinished;

    @Column(nullable = true)
    private Integer currentHole;
}
