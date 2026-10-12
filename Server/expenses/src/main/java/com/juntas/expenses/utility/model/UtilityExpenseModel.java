package com.juntas.expenses.utility.model;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "utility_expense", schema = "JVDB_Expense")
public class UtilityExpenseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, nullable = false)
    private LocalDate emissionDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal bill;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private UtilityType utilType;

    @Column(nullable = false)
    private Long forUserId;

    @Column(nullable = false, length = 120)
    private String forEmail; 

    @Column(nullable = false)
    private Long neighborhoodId;
}
