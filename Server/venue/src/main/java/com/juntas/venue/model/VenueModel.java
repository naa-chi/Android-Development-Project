package com.juntas.venue.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor 
@NoArgsConstructor 
@Data
@Entity
@Table(name = "Venue", schema = "JVDB_Venue")
@Getter 
@Setter 
public class VenueModel {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(unique=false, nullable = false)
    private String venueName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private VenueType type;

    @NotNull
    @Column(nullable = false, unique = false)
    private Long neighbourhoodId;

    @Min(value=1)
    @Column(unique=false, nullable = false)
    private Long venueSizeMSquared;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerHour;

    @Positive 
    @Column(nullable = false)
    private int capacity;


}
