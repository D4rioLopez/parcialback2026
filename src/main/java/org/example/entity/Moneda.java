package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "COTIZACIONES")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Moneda {
    @Id
    @Column(name = "MONEDA", length = 10)
    private String moneda;

    @Column(name = "TASA_CAMBIO", nullable = false, precision = 20, scale = 6)
    private BigDecimal tasaCambio;


}
