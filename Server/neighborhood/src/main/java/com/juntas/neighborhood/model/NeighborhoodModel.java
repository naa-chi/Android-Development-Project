package com.juntas.neighborhood.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "neighborhood", schema = "JVDB_Neighborhood")
public class NeighborhoodModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    private String neighborhoodName;

    @Positive
    @Column(nullable = false)
    private Long populationNumber; //I don't know if we need a population number. It doesn't feel relevant? It never really should come into play besides safekeeping records. Whatever
}