package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Table(name = "COTIZACIONES")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cotizacion {
    @Id
    @Column(name = "MONEDA",length = 10, nullable = false)
    private String moneda;

    @Column(name = "TASA_CAMBIO", nullable = false,precision = 19,scale = 4)
    private BigDecimal tasaCambio;
}