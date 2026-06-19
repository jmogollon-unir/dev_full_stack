package com.relato_papel.users.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * accessToken = JWT firmado (claims userId/cif); el API Gateway lo reenvía en el header accessToken.
 * sessionToken = token opaco en Redis; el cliente lo envía como Authorization: Bearer al gateway.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String sessionToken;
}
