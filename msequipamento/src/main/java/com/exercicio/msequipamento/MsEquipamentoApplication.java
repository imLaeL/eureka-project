package com.exercicio.msequipamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsEquipamentoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsEquipamentoApplication.class, args);
    }
}
