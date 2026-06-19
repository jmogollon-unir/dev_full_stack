package com.relatos_papel.gateway_secured.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionValidationResponse {
    private String accessToken;
    private String refreshToken;
}
