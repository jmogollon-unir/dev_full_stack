package com.relato_papel.users.controller;

import com.relato_papel.users.controller.dto.LoginRequest;
import com.relato_papel.users.controller.dto.TokenResponse;
import com.relato_papel.users.controller.dto.RefreshResponse;
import com.relato_papel.users.service.AuthService;
import com.relato_papel.users.service.model.SessionValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final AuthService authService;

    /**
     * Crea un nuevo token de autenticación
     */
    @PostMapping
    public ResponseEntity<?> createToken(@RequestBody LoginRequest loginRequest) {

        Optional<String> tokenIdOptional = authService.createPhantomToken(
            loginRequest.getUsername(),
            loginRequest.getPassword()
        );

        if (tokenIdOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Datos de autenticación no válidos");
        }

        return ResponseEntity.ok(new TokenResponse(tokenIdOptional.get()));
    }

    /**
     * Renueva un token existente (solo si el JWT actual sigue siendo válido)
     */
    @PostMapping("/{tokenId}/renewals")
    public ResponseEntity<?> refreshToken(@PathVariable String tokenId) {

        Optional<String> tokenIdOptional = authService.refreshPhantomToken(tokenId);
        if (tokenIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no valido o expirado");
        }
        return ResponseEntity.ok(new TokenResponse(tokenIdOptional.get()));
    }

    /**
     * Valida un token existente (solo devuelve 200 o 410)
     */
    @GetMapping("/{tokenId}")
    public ResponseEntity<?> validateToken(@PathVariable String tokenId) {
        SessionValidationResponse result = authService.validatePhantomToken(tokenId);

        return switch (result.status()) {
            case VALID -> ResponseEntity.ok(new TokenResponse(result.redisSessionData().getAccessToken()));
            case EXPIRED, NOT_FOUND -> ResponseEntity.status(HttpStatus.GONE).build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }
}
