package com.banco.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loading_errors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LoadErrorsEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idError;

    private String tipoError;
    private String numeroIdentificacion;
    private String descripcion;

    @Column(updatable = false)
    private LocalDateTime fechaError;

    @PrePersist
    protected void onCreate() {
        fechaError = LocalDateTime.now();
    }
}
