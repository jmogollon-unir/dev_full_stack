package com.relato_papel.users.controller;

import com.relato_papel.users.controller.dto.UserProfileDto;
import com.relato_papel.users.repository.users.UserJpaRepository;
import com.relato_papel.users.repository.users.model.User;
import com.relato_papel.users.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserJpaRepository userRepository;
    private final JwtUtils jwtUtils;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable String userId,
                                            @RequestHeader(value = "Authorization", required = false) String authorization,
                                            @RequestHeader(value = "accessToken", required = false) String accessTokenHeader) {

        // El gateway inyecta el JWT en accessToken; el cliente suele enviar el opaco en Authorization.
        String token = accessTokenHeader;
        if (token == null || token.isBlank()) {
            token = resolveBearer(authorization);
        }

        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token de autorización requerido");
        }

        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");
        }

        String tokenUserId = jwtUtils.getCifFromToken(token);
        if (tokenUserId == null || !tokenUserId.equals(userId)) {
            log.warn("Intento de acceso no autorizado al perfil de usuario: {}", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado para acceder a este perfil");
        }

        Optional<User> userOptional = userRepository.findByCif(String.valueOf(userId));
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        UserProfileDto profile = UserProfileDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .cif(user.getCif())
                .sector(user.getSector())
                .employees(user.getEmployees())
                .foundedYear(user.getFoundedYear())
                .company(UserProfileDto.Company.builder()
                        .name(user.getName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .address(user.getAddress())
                        .sector(user.getSector())
                        .employees(user.getEmployees())
                        .foundedYear(user.getFoundedYear())
                        .build())
                .build();

        return ResponseEntity.ok(profile);
    }

    private static String resolveBearer(String authorization) {
        if (authorization == null || !authorization.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return null;
        }
        return authorization.substring(7).trim();
    }
}
