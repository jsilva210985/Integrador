package com.integrador.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integrador.models.Configuracion;

public interface ConfiguracionRepository extends JpaRepository<Configuracion, Integer> {
	Configuracion findByTipoAndServicio(String tipo, String servicio);
}
