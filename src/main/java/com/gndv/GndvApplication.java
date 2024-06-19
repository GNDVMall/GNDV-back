package com.gndv;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
public class GndvApplication {

    public static void main(String[] args) {
        SpringApplication.run(GndvApplication.class, args);
    }
}
