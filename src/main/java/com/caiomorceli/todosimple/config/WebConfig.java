package com.caiomorceli.todosimple.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Arquivo de configuração CORS.

@Configuration
@EnableWebMvc   // habilita as configurações padrão do Spring MVC, e registra os componentes básicos necessários
public class WebConfig implements WebMvcConfigurer {
    
    // Liberado para conexão de qualquer local (configuração simples). 
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS");
    }
}
