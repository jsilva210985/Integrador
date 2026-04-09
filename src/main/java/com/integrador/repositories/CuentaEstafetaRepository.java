package com.integrador.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CuentaEstafetaRepository extends JpaRepository<com.integrador.models.CuentaEstafeta, Integer> {
	public com.integrador.models.CuentaEstafeta findByCuenta(String cuenta);
}
