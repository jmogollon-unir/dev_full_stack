package com.relatos_papel.communications.chat.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String type;    // Puede ser: "chunk" (pedacito de texto), "done" (terminó), "error"
    private String content; // El texto en sí
}