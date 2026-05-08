package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Quando o navegador/app pedir "/uploads/...", o Spring busca na pasta "fotos-eventos"
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:fotos-eventos/");
    }
}