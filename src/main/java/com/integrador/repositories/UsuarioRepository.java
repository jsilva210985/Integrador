package com.integrador.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.integrador.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	Usuario findByUsuarioAndContrasena(String usuario, String contrasena);
	Usuario findByUsuario(String usuario);
	Usuario findByIdUsuario(int idUsuario);
}
