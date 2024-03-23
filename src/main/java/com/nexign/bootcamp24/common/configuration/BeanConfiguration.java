package com.nexign.bootcamp24.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * Данный класс служит неким конфигуратором для бинов
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public Random random() {
        return new Random();
    }
}
