package org.bf.spotservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "org.bf.spotservice",
        "org.bf.global"
})
public class SpotServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotServiceApplication.class, args);
    }

}