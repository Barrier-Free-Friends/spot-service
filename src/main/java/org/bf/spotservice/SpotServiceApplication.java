package org.bf.spotservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = {
        "org.bf.spotservice",
        "org.bf.global"
})
public class SpotServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotServiceApplication.class, args);
    }

}