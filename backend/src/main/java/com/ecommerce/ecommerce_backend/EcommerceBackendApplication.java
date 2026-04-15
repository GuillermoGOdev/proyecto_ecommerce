package com.ecommerce.ecommerce_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

import com.ecommerce.ecommerce_backend.builder.ComputadoraBuilder;
import com.ecommerce.ecommerce_backend.factory.*;
import com.ecommerce.ecommerce_backend.model.Computadora;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class EcommerceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceBackendApplication.class, args);

		//Elegimos que fabrica queremos emplear
		ComponentesFactory miFactory = new GamerFactory();

		//Usamos el builder para armar la PC
		Computadora miComputadora = new ComputadoraBuilder("PC Gamer Pro 2026").
		conProcesador(miFactory.crearProcesador()).
		conMemoriaRam(miFactory.crearMemoriaRam()).
		conTarjetaVideo(miFactory.crearTarjetaVideo()).build();

		// 3. Mostramos el resultado
        System.out.println("--- CONFIGURACIÓN CREADA ---");
        System.out.println("Nombre: " + miComputadora.getNombreConfiguracion());
        System.out.println("CPU: " + miComputadora.getProcesador().getNombre());
        System.out.println("GPU: " + miComputadora.getTarjetaVideo().getNombre());
        System.out.println("Precio Total: $" + miComputadora.getPrecioTotal());
	}

}
