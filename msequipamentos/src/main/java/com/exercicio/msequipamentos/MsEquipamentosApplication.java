package com.exercicio.msequipamentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsEquipamentosApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsEquipamentosApplication.class, args);
    }
}
