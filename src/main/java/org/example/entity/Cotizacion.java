package org.example.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "COTIZACIONES",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"ID_TARJETA","MES","ANIO"})})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "MONEDA",nullable = false)
    private Moneda moneda;

    @Column(name = "TASA_CAMBIO", nullable = false)
    private float tasaCambio;

}