package com.juntas.users.repository;

import com.juntas.users.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByRutNumeric(String rutNumeric); // Thank john programming for making these easy to copy and paste across services. 
}