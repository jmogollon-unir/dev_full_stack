package com.relatos_papel.users.controller.dto;

import lombok.Data;

/**
 * Credenciales de login: {@code username} es el correo del usuario y {@code password} la contraseña
 * (el front envía su MD5; también se acepta en texto plano, p. ej. desde Postman).
 */
@Data
public class LoginRequest {

    private String username;
    private String password;
}
