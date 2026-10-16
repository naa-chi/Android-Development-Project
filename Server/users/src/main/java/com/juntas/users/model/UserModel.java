package com.juntas.users.model;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "appuser", schema = "JVDB_Users")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 8)
    private String rutNumeric;
    
    @Column(nullable = false, unique = false, length = 1)
    private String dv;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private Role role;

    @Column(nullable = false)
    private Long neighborhoodId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false, length = 100)
    private String passwordHash;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
    } // This one's ai gen'd so i hope this segment doesn't break
}
