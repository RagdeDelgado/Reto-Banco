package com.banco.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountsEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCuenta;

    @Column(unique = true, nullable = false)
    private String numeroCuenta;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private ClientsEntity cliente;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TipoCuenta tipoCuenta = TipoCuenta.AHORROS;

    @Column(precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal saldo = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorNomina;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EstadoCuenta estado = EstadoCuenta.ACTIVO;

    @Column(updatable = false)
    private Date fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = new Date();
    }

    public enum TipoCuenta {
        AHORROS, CORRIENTE
    }

    public enum EstadoCuenta {
        ACTIVO, INACTIVO
    }
}
