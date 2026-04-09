package com.integrador.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CuentaEstafetaV2Repository extends JpaRepository<com.integrador.models.CuentaEstafetaV2, Integer> {
	public com.integrador.models.CuentaEstafetaV2 findByCuenta(String cuenta);
}
