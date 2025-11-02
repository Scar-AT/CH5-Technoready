package com.techready.meli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MeliApplication {
    public static void main(String[] args) {
        // Force PostgreSQL driver to treat Strings as text
        System.setProperty("spring.datasource.url",
                "jdbc:postgresql://localhost:5432/meli_prod?stringtype=unspecified");

        SpringApplication.run(MeliApplication.class, args);
    }
}
