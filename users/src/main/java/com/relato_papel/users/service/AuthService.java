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

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserJpaRepository userRepository;
    private final RedisSessionRepository redisSessionRepository;
    private final JwtUtils jwtUtils;

    public Optional<String> createPhantomToken(String username, String password) {

        Optional<User> userOptional = userRepository.findByCif(username);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        User user = userOptional.get();
        if (!user.getPassword().equals(password)) {
            return Optional.empty();
        }

        String accessToken = jwtUtils.generateAccessToken(username, user.getId());
        String opaqueToken = UUID.randomUUID().toString();

        RedisSessionData redisSessionData = new RedisSessionData(opaqueToken, accessToken, username, user.getId());
        redisSessionRepository.saveSession(redisSessionData);
        return Optional.of(opaqueToken);
    }

    public Optional<String> getJwtForSession(String opaqueToken) {
        return redisSessionRepository.findSession(opaqueToken).map(RedisSessionData::getAccessToken);
    }

    public SessionValidationResponse validatePhantomToken(String opaqueToken) {

        Optional<RedisSessionData> sessionOptional = redisSessionRepository.findSession(opaqueToken);
        if (sessionOptional.isEmpty()) {
            return new SessionValidationResponse(SessionStatus.NOT_FOUND, null);
        }
        RedisSessionData session = sessionOptional.get();

        if (jwtUtils.validateToken(session.getAccessToken())) {
            return new SessionValidationResponse(SessionStatus.VALID, session);
        }

        redisSessionRepository.deleteSession(opaqueToken);
        return new SessionValidationResponse(SessionStatus.EXPIRED, null);
    }

    public Optional<String> refreshPhantomToken(String opaqueToken) {

        Optional<RedisSessionData> sessionOptional = redisSessionRepository.findSession(opaqueToken);
        if (sessionOptional.isEmpty()) {
            return Optional.empty();
        }
        RedisSessionData currentSession = sessionOptional.get();

        if (!jwtUtils.validateToken(currentSession.getAccessToken())) {
            redisSessionRepository.deleteSession(opaqueToken);
            return Optional.empty();
        }

        String newAccessToken = jwtUtils.generateAccessToken(currentSession.getUsername(), currentSession.getUserId());
        String newOpaqueToken = UUID.randomUUID().toString();

        redisSessionRepository.deleteSession(opaqueToken);

        RedisSessionData newSession = new RedisSessionData(
                newOpaqueToken, newAccessToken,
                currentSession.getUsername(), currentSession.getUserId());

        redisSessionRepository.saveSession(newSession);
        return Optional.of(newOpaqueToken);
    }
}
