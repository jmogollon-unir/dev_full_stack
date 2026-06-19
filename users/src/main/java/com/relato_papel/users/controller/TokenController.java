package com.relato_papel.users.controller;

import com.relato_papel.users.controller.dto.LoginRequest;
import com.relato_papel.users.controller.dto.TokenResponse;
import com.relato_papel.users.service.AuthService;
import com.relato_papel.users.service.model.SessionValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> createToken(@RequestBody LoginRequest loginRequest) {

        Optional<String> tokenIdOptional = authService.createPhantomToken(
            loginRequest.getUsername(),
            loginRequest.getPassword()
        );

        if (tokenIdOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Datos de autenticación no válidos");
        }

        String sessionToken = tokenIdOptional.get();
        String jwt = authService.getJwtForSession(sessionToken).orElse(null);
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo obtener el token de acceso");
        }

        return ResponseEntity.ok(new TokenResponse(jwt, sessionToken));
    }

    @PostMapping("/{tokenId}/renewals")
    public ResponseEntity<?> refreshToken(@PathVariable String tokenId) {

        Optional<String> tokenIdOptional = authService.refreshPhantomToken(tokenId);
        if (tokenIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no valido o expirado");
        }
        String newSession = tokenIdOptional.get();
        String jwt = authService.getJwtForSession(newSession).orElse(null);
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no valido o expirado");
        }
        return ResponseEntity.ok(new TokenResponse(jwt, newSession));
    }

    @GetMapping("/{tokenId}")
    public ResponseEntity<?> validateToken(@PathVariable String tokenId) {
        SessionValidationResponse result = authService.validatePhantomToken(tokenId);

        return switch (result.status()) {
            case VALID -> {
                String jwt = result.redisSessionData().getAccessToken();
                yield ResponseEntity.ok(new TokenResponse(jwt, tokenId));
            }
            case EXPIRED, NOT_FOUND -> ResponseEntity.status(HttpStatus.GONE).build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }
}
