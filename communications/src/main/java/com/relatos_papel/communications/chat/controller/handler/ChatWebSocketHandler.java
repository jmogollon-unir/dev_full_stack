package com.relatos_papel.communications.chat.controller.handler;

import com.relatos_papel.communications.chat.controller.model.ChatRequest;
import com.relatos_papel.communications.chat.controller.model.ChatResponse;
import lombok.RequiredArgsConstructor;
import com.relatos_papel.communications.chat.service.StreamGeminiChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final StreamGeminiChatService geminiChatService;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Nuevo cliente conectado al chat WS: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatRequest chatRequest;
        try {
            chatRequest = objectMapper.readValue(message.getPayload(), ChatRequest.class);
        } catch (Exception e) {
            sendJsonResponse(session, new ChatResponse("error", "Formato JSON inválido"));
            return;
        }

        geminiChatService.getStreamResponse(chatRequest.getMessage())
                .subscribe(
                        chunk -> sendJsonResponse(session, new ChatResponse("chunk", chunk)),
                        error -> sendJsonResponse(session, new ChatResponse("error", "Error interno comunicándose con Gemini")),
                        () -> sendJsonResponse(session, new ChatResponse("done", ""))
                );
    }

    // CORRECCIÓN VITAL: "synchronized" es obligatorio en Spring WebSocket
    // cuando se envían mensajes desde streams reactivos o hilos asíncronos.
    private synchronized void sendJsonResponse(WebSocketSession session, ChatResponse response) {
        try {
            if (session.isOpen()) {
                String jsonStr = objectMapper.writeValueAsString(response);
                session.sendMessage(new TextMessage(jsonStr));
            }
        } catch (IOException e) {
            log.error("Error enviando respuesta JSON por WebSocket", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Cliente desconectado del chat WS: {}", session.getId());
    }
}