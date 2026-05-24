package org.example.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "TITULAR",length = 100,nullable = false)
    private String titular;

    @Column(name = "LIMITE_CREDITO",nullable = false)
    private double limiteCredito;

}
