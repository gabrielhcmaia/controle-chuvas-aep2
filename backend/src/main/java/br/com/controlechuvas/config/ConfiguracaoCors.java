package br.com.controlechuvas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfiguracaoCors implements WebMvcConfigurer {

    private static final String ORIGEM_FRONTEND = "http://localhost:4200";

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(ORIGEM_FRONTEND)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE");
    }
}
