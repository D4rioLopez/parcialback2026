package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "CONSUMOS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Consumo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_TARJETA",nullable = false)
    private Tarjeta tarjeta;

    @Column(name = "MONEDA", length = 10, nullable = false) // asegurar nombre exacto
    private String moneda;

    @Column(name = "MONTO", precision = 20, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(name = "DIA", nullable = false)
    private Integer dia;

    @Column(name = "MES", nullable = false)
    private Integer mes;

    @Column(name = "ANIO", nullable = false)
    private Integer anio;

    @Column(name = "RUBRO")
    private String rubro;

}

