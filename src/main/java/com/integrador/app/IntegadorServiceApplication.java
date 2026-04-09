package com.integrador.app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@SpringBootApplication

@PropertySources({
	@PropertySource(value = "classpath:application.properties"), //Properties by default
	@PropertySource(value = "classpath:properties/${integrador.app}app.properties") //Propierties by environment
})

@ComponentScan({
	"com.integrador.app",
	"com.integrador.services",
	"com.integrador.restcontroller.app",
	"com.integrador.restcontroller.fedex",
	"com.integrador.restcontroller.estafeta",
	"com.integrador.restcontroller.dhl",
	"com.xcustom.alan.businessrules",
	"com.integrador.util"
})

@EntityScan({"com.integrador.models"})
@EnableJpaRepositories("com.integrador.repositories")

public class IntegadorServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(IntegadorServiceApplication.class, args);
	}
}
