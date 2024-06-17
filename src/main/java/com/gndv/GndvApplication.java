package com.gndv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class GndvApplication {

    public static void main(String[] args) {
        SpringApplication.run(GndvApplication.class, args);
    }
}
