package com.relato_papel.users.service;

import com.relato_papel.users.repository.users.UserJpaRepository;
import com.relato_papel.users.repository.users.model.User;
import com.relato_papel.users.repository.sessions.RedisSessionRepository;
import com.relato_papel.users.repository.sessions.model.RedisSessionData;
import com.relato_papel.users.service.model.SessionStatus;
import com.relato_papel.users.service.model.SessionValidationResponse;
import com.relato_papel.users.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserJpaRepository userRepository;
    private final RedisSessionRepository redisSessionRepository;
    private final JwtUtils jwtUtils;


    /**
     * Autentica a un usuario y crea una sesión con tokens JWT.
     * @param username - CIF del usuario
     * @param password - Contraseña del usuario
     * @return ID de sesión si la autenticación es exitosa, vacío en caso contrario
     */
    public Optional<String> createPhantomToken(String username, String password) {

        Optional<User> userOptional = userRepository.findByCif(username);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        User user = userOptional.get();
        
        // 1. Convertimos la contraseña de texto plano (Postman) a MD5
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        
        // 2. Comparamos el MD5 generado con el MD5 de la base de datos
        if (!user.getPassword().equals(md5Password)) {
            return Optional.empty();
        }

        // Generar token JWT con duración de 5 minutos
        String accessToken = jwtUtils.generateAccessToken(username, user.getId());

        // Generar ID de sesión único (token opaco)
        String opaqueToken = UUID.randomUUID().toString();

        // Guardar sesión en Redis con TTL automático de 5 minutos
        RedisSessionData redisSessionData = new RedisSessionData(opaqueToken, accessToken, username, user.getId());
        redisSessionRepository.saveSession(redisSessionData);
        return Optional.of(opaqueToken);
    }

    /**
     * Valida una sesión existente.
     * @param opaqueToken - ID de la sesión a validar
     * @return Estado de la sesión y datos asociados (200 o 410 únicamente)
     */
    public SessionValidationResponse validatePhantomToken(String opaqueToken) {

        Optional<RedisSessionData> sessionOptional = redisSessionRepository.findSession(opaqueToken);
        if (sessionOptional.isEmpty()) {
            return new SessionValidationResponse(SessionStatus.NOT_FOUND, null);
        }
        RedisSessionData session = sessionOptional.get();

        // Verificar token de acceso
        if (jwtUtils.validateToken(session.getAccessToken())) {
            return new SessionValidationResponse(SessionStatus.VALID, session);
        }

        // Si el token no es válido, eliminar la sesión y retornar expirada
        redisSessionRepository.deleteSession(opaqueToken);
        return new SessionValidationResponse(SessionStatus.EXPIRED, null);
    }

    /**
     * Refresca una sesión existente generando nuevos tokens.
     * Solo funciona si el JWT actual sigue siendo válido.
     * @param opaqueToken - ID de la sesión actual
     * @return Par de nuevo token opaco y access token si la sesión es válida, vacío en caso contrario
     */
    public Optional<String> refreshPhantomToken(String opaqueToken) {

        Optional<RedisSessionData> sessionOptional = redisSessionRepository.findSession(opaqueToken);
        if (sessionOptional.isEmpty()) {
            return Optional.empty();
        }
        RedisSessionData currentSession = sessionOptional.get();

        // Verificar que el JWT actual sea válido para permitir el refresh
        if (!jwtUtils.validateToken(currentSession.getAccessToken())) {
            // Si el JWT no es válido, eliminar la sesión
            redisSessionRepository.deleteSession(opaqueToken);
            return Optional.empty();
        }

        // Generar nuevo token JWT con duración de 5 minutos
        String newAccessToken = jwtUtils.generateAccessToken(currentSession.getUsername(), currentSession.getUserId());

        // Generar nuevo token opaco para el cliente
        String newOpaqueToken = UUID.randomUUID().toString();

        // Eliminar la sesión actual
        redisSessionRepository.deleteSession(opaqueToken);

        // Crear una nueva sesión con nuevos tokens
        RedisSessionData newSession = new RedisSessionData(
                newOpaqueToken, newAccessToken,
                currentSession.getUsername(), currentSession.getUserId());

        redisSessionRepository.saveSession(newSession);
        // Retornar tanto el nuevo token opaco como el access token
        return Optional.of(newOpaqueToken);
    }
}
