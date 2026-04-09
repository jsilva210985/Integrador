package com.integrador.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.integrador.models.UsuarioAlan;

public interface UsuarioAlanRepository extends JpaRepository<UsuarioAlan, Integer> {
	UsuarioAlan findByUsuarioAndContrasena(String usuario, String contrasena);
	UsuarioAlan findByUsuario(String usuario);
	UsuarioAlan findByIdUsuario(int idUsuario);
}
