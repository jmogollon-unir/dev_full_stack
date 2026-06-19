package com.relato_papel.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
<<<<<<< HEAD
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/tokens/**").permitAll()
                        .requestMatchers("/api/v1/users/**").permitAll()
                        .anyRequest().denyAll()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
=======
            // 1. Desactivamos CSRF (Obligatorio para que Postman y Frontend puedan hacer POST)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. Usamos modo Stateless porque gestionaremos la sesión con nuestros propios Tokens en Redis
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 3. Configuramos qué rutas son públicas y cuáles no
            .authorizeHttpRequests(auth -> auth
                // Permitimos acceso a los endpoints de tokens (Login, Refresh, Validar)
                .requestMatchers("/api/tokens", "/api/tokens/**").permitAll()
                
                // Permitimos acceso al endpoint de usuarios (porque tu UserController ya valida el JWT manualmente)
                .requestMatchers("/api/users", "/api/users/**").permitAll()
                
                // Todo lo demás sí requerirá autenticación
                .anyRequest().authenticated() 
            );

        return http.build();
    }
}
>>>>>>> 9f533c0d49dc694d06b34dec912d4f9f1d3571cf
