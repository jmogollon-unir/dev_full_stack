package com.relatos_papel.communications.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GeminiRequestDto {
    @JsonProperty("system_instruction")
    private SystemInstruction systemInstruction;
    private List<Content> contents;

    @Data
    @Builder
    public static class SystemInstruction {
        private List<Part> parts;
    }

    @Data
    @Builder
    public static class Content {
        private String role;
        private List<Part> parts;
    }

    @Data
    @Builder
    public static class Part {
        private String text;
    }
}