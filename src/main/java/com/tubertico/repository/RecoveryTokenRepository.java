package com.tubertico.repository;

import com.tubertico.model.RecoveryToken;
import com.tubertico.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecoveryTokenRepository extends JpaRepository<RecoveryToken, Long> {
    Optional<RecoveryToken> findByToken(String token);

    // 👇 Este es el que te faltaba
    Optional<RecoveryToken> findByUsuario(Usuario usuario);
}
