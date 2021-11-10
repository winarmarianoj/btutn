package com.utn.bolsadetrabajo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BolsadetrabajoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BolsadetrabajoApplication.class, args);
    }

}
