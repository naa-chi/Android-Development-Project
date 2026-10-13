package com.juntas.expenses.event.model;
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
public class EventExpenseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, nullable = false)
    private LocalDate emissionDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal bill;

    @Column(nullable = false, unique = false)
    private Long eventId;

    @Column(nullable = false)
    private Long forUserId;

    @Column(nullable = false, length = 120)
    private String forEmail; 

    @Column(nullable = false)
    private Long neighborhoodId;
}
