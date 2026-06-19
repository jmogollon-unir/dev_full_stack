package com.relatos_papel.api_gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Cuerpo JSON devuelto por el microservicio users al validar o crear sesión.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UsersTokenResponse(String accessToken, String sessionToken) {
}
