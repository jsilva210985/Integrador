package com.integrador.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.integrador.models.Atributo;

public interface AtributoRepository extends JpaRepository<Atributo, Integer> {
	List<Atributo> findByTipo(String tipo);
}