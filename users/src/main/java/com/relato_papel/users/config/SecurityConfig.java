package com.relato_papel.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
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