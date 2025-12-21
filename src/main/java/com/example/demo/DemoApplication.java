package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
        "com.example.demo.candidat.repository",
        "com.example.demo.professeur.repository",
        "com.example.demo.directeur.ced.repository",
        "com.example.demo.security.user"          // (Vérifiez si vos repos sécurité sont bien ici, souvent c'est .security.repository)
})
@EntityScan(basePackages = {
        "com.example.demo.candidat.model",
        "com.example.demo.professeur.model",
        "com.example.demo.ced.model",
        "com.example.demo.security.user"          // Pour vos User/Role
})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}