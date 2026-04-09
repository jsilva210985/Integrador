package com.integrador.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.integrador.models.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {
	Token findByIdUsuarioAndToken(int idUsuario, String token);
}
