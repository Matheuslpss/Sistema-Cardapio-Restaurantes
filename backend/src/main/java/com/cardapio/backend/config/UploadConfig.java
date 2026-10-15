package com.cardapio.backend.config;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UploadConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String caminhoUploads = Paths.get("uploads").toAbsolutePath().normalize().toUri().toString();
        registry.addResourceHandler("/imagens/**")
                .addResourceLocations(caminhoUploads);
    }
}
