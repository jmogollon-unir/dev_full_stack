package com.relatos_papel.communications.chat.service;

import com.relatos_papel.communications.chat.config.GeminiProperties;
import com.relatos_papel.communications.chat.model.GeminiRequestDto;
import com.relatos_papel.communications.chat.model.GeminiResponseDto;
import com.relatos_papel.communications.chat.prompt.SystemPromptProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamGeminiChatService {

    private final GeminiProperties geminiProperties;
    private final SystemPromptProvider promptProvider;
    private final WebClient webClient = WebClient.create();

    public Flux<String> getStreamResponse(String userMessage) {
        GeminiRequestDto requestBody = buildGeminiRequest(userMessage);
        String baseUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";
        String fullUrl = baseUrl + "?key=" + geminiProperties.getKey();

        return webClient.post()
                .uri(fullUrl)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), this::handleGeminiError)
                .bodyToMono(GeminiResponseDto.class)
                .map(this::extractTextFromResponse)
                .filter(text -> text != null && !text.isEmpty())
                .flux();
    }

    private Mono<? extends Throwable> handleGeminiError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .defaultIfEmpty("")
                .map(body -> {
                    log.error("Gemini API error {}: {}", response.statusCode(), body);
                    return new IllegalStateException("Gemini API error " + response.statusCode());
                });
    }

    private GeminiRequestDto buildGeminiRequest(String userMessage) {
        GeminiRequestDto.Part systemPart = GeminiRequestDto.Part.builder()
                .text(promptProvider.getSystemInstruction())
                .build();
        GeminiRequestDto.SystemInstruction systemInstruction = GeminiRequestDto.SystemInstruction.builder()
                .parts(List.of(systemPart))
                .build();

        GeminiRequestDto.Part userPart = GeminiRequestDto.Part.builder()
                .text(userMessage)
                .build();
        GeminiRequestDto.Content userContent = GeminiRequestDto.Content.builder()
                .role("user")
                .parts(List.of(userPart))
                .build();

        return GeminiRequestDto.builder()
                .systemInstruction(systemInstruction)
                .contents(List.of(userContent))
                .build();
    }

    private String extractTextFromResponse(GeminiResponseDto responseDto) {
        if (responseDto == null || responseDto.getCandidates() == null || responseDto.getCandidates().isEmpty()) {
            return "";
        }

        GeminiResponseDto.Content content = responseDto.getCandidates().get(0).getContent();
        if (content == null || content.getParts() == null || content.getParts().isEmpty()) {
            return "";
        }

        return content.getParts().get(0).getText();
    }
}
