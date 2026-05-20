package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "LIQUIDACIONES")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Liquidacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LIQUIDACION")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TARJETA", nullable = false)
    private Tarjeta tarjeta;

    @Column(name = "MES", nullable = false)
    private Integer mes;

    @Column(name = "ANIO", nullable = false)
    private Integer anio;

    @Column(name = "TOTAL_A_PAGAR",nullable = false)
    private BigDecimal totalAPagar;

    @Column(name = "TOTAL_CONSUMOS",nullable = false)
    private BigDecimal totalConsumos;

    @Column(name = "TOTAL_IMPUESTOS",nullable = false)
    private BigDecimal totalImpuestos;

    @Column(name = "TOTAL_DESCUENTOS",nullable = false)
    private BigDecimal totaDesuestos;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_liquidacion_tarjeta", nullable = false)
    private Tarjeta tarjeta_id;
}
