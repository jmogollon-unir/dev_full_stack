package com.relato_papel.users.service.model;

import com.relato_papel.users.repository.sessions.model.RedisSessionData;

public record SessionValidationResponse(SessionStatus status, RedisSessionData redisSessionData) {

}
