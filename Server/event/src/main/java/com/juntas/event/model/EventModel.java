package com.juntas.event.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor 
@NoArgsConstructor 
@Data 
@Entity
@Table(name = "Event", schema = "JVDB_Event")
@Getter 
@Setter 
public class EventModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank 
    @Size(min = 5, max = 40)
    @Column(unique = false, nullable = false) //Redundant nulable check but whtaever
    private String title;

    @NotNull
    @Size(max=150)
    @Column(unique = false, nullable = false)
    private String description;

    @Column(nullable = false)
    private Long venueId;

    @Column(nullable = false)
    private Long neighborId; //the USER

    @NotNull 
    @Column(unique = false, nullable = false, name = "scheduled_date")
    private LocalDate date;

    @NotNull 
    @Column(unique = false, nullable = false)
    private LocalTime startTime;

    @NotNull 
    @Column(unique = false, nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private EventType eventType;
}
