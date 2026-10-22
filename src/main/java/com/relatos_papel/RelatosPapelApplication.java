package com.relatos_papel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación única de Relatos de Papel: API (catalogue, orders, users) + front React empaquetado
 * en el mismo JAR, con una sola base de datos MySQL.
 */
@SpringBootApplication
public class RelatosPapelApplication {

    public static void main(String[] args) {
        SpringApplication.run(RelatosPapelApplication.class, args);
    }
}
