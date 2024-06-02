package com.gndv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class GndvApplication {

    public static void main(String[] args) {
        SpringApplication.run(GndvApplication.class, args);
    }

}
