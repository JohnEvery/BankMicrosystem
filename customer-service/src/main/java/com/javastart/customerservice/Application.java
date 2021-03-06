package com.javastart.customerservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SpringBootApplication
@EnableRabbit
@ConfigurationProperties("application")
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
