package com.gndv.config;

import com.siot.IamportRestClient.IamportClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${iamport.api.key}")
    private String apiKey;

    @Value("${iamport.secret.key}")
    private String secretKey;

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, secretKey);
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
