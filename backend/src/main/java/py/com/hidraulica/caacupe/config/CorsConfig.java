package py.com.hidraulica.caacupe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    // Frontend dev
    config.setAllowedOrigins(List.of("http://localhost:4200"));

    // Métodos permitidos (incluye OPTIONS para preflight)
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

    // Headers que el browser puede enviar (incluye Authorization)
    config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));

    // Headers que el browser puede leer desde la respuesta
    config.setExposedHeaders(List.of("Authorization"));

    // Si más adelante usás cookies/sesión, esto debe ser true y NO usar "*"
    config.setAllowCredentials(false);

    // Cache del preflight
    config.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
