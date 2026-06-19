package com.relatos_papel.communications.chat.config;

import com.relatos_papel.communications.chat.controller.handler.ChatWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // AHORA COINCIDE CON LA RUTA QUE DEJA PASAR EL GATEWAY
        registry.addHandler(chatWebSocketHandler, "/ws-api/v1/communications/chat")
                .setAllowedOrigins("*");
    }
}