package com.relato_papel.users.repository.sessions.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisSessionData {

    private String sessionId;
    private String accessToken;
    private String username;
    private Integer userId;
    private long createdAt;

    public RedisSessionData(String sessionId, String accessToken, String username, Integer userId) {
        this.sessionId = sessionId;
        this.accessToken = accessToken;
        this.username = username;
        this.userId = userId;
        this.createdAt = System.currentTimeMillis();
    }
}
