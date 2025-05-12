package com.ayd.game_service.holes.models;

import org.hibernate.annotations.DynamicUpdate;

import com.ayd.shared.models.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
public class Hole extends Auditor {
    @Column(nullable = false, unique = true)
    private Integer number;

    @Column(nullable = false, unique = true)
    private String name;
}
