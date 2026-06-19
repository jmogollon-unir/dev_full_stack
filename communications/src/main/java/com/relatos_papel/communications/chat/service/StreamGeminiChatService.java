package com.relatos_papel.communications.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.relatos_papel.communications.chat.config.GeminiProperties;
import com.relatos_papel.communications.chat.model.GeminiRequestDto;
import com.relatos_papel.communications.chat.model.GeminiResponseDto;
import com.relatos_papel.communications.chat.prompt.SystemPromptProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamGeminiChatService {

    private final GeminiProperties geminiProperties;
    private final SystemPromptProvider promptProvider;
    private final ObjectMapper objectMapper;
    private final WebClient webClient = WebClient.create();

    public Flux<String> getStreamResponse(String userMessage) {
        GeminiRequestDto requestBody = buildGeminiRequest(userMessage);

        // CORRECCIÓN VITAL PARA EVITAR EL 404:
        // Forzamos la estructura correcta de la URL de streaming para la API de Google
        String baseUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:streamGenerateContent";
        String fullUrl = baseUrl + "?alt=sse&key=" + geminiProperties.getKey();

        return webClient.post()
                .uri(fullUrl)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {})
                .map(event -> extractTextFromJson(event.data()))
                .filter(text -> text != null && !text.isEmpty()); // Evitamos enviar strings vacíos
    }

    private GeminiRequestDto buildGeminiRequest(String userMessage) {
        GeminiRequestDto.Part systemPart = GeminiRequestDto.Part.builder().text(promptProvider.getSystemInstruction()).build();
        GeminiRequestDto.SystemInstruction systemInstruction = GeminiRequestDto.SystemInstruction.builder().parts(List.of(systemPart)).build();

        GeminiRequestDto.Part userPart = GeminiRequestDto.Part.builder().text(userMessage).build();
        GeminiRequestDto.Content userContent = GeminiRequestDto.Content.builder().role("user").parts(List.of(userPart)).build();

        return GeminiRequestDto.builder()
                .systemInstruction(systemInstruction)
                .contents(List.of(userContent))
                .build();
    }

    private String extractTextFromJson(String jsonChunk) {
        if (jsonChunk == null || jsonChunk.isBlank()) {
            return "";
        }
        try {
            GeminiResponseDto responseDto = objectMapper.readValue(jsonChunk, GeminiResponseDto.class);

            if (responseDto.getCandidates() != null && !responseDto.getCandidates().isEmpty()) {
                List<GeminiResponseDto.Part> parts = responseDto.getCandidates().get(0).getContent().getParts();
                if (parts != null && !parts.isEmpty()) {
                    return parts.get(0).getText();
                }
            }
        } catch (Exception e) {
            log.error("Error parseando el chunk de Gemini a DTO: {}", jsonChunk, e);
        }
        return "";
    }
}