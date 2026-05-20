package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "TARJETAS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tarjeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ID")
    private Long id;
    @Column(name = "NUMERO", unique = true, nullable = false, length = 16)
    private String numero;

    @Column(name = "TITULAR")
    private String titular;

    @Column(name = "LIMITE_CREDITO", precision = 20, scale = 2)
    private BigDecimal limiteCredito;

    @OneToMany(mappedBy = "tarjeta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consumo> consumos;


}
