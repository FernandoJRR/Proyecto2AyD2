package com.ayd.reservation_service.shared.models;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditor {

    /**
     * Identificador único de la entidad. Se genera automáticamente utilizando
     * el tipo UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, unique = true, length = 50)
    private String id;

    /**
     * Fecha y hora en que se creó el registro. Este campo es asignado
     * automáticamente y no puede ser actualizado.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    /**
     * Fecha y hora de la última actualización del registro.
     */
    @Column
    @LastModifiedDate
    private LocalDate updateAt;

    public Auditor(String id) {
        this.id = id;
    }

}
