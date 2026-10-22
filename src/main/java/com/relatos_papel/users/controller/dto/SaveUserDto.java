package com.relatos_papel.users.controller.dto;

import lombok.Data;

/** Cuerpo para crear (POST), reemplazar (PUT) o actualizar parcialmente (PATCH) un usuario. */
@Data
public class SaveUserDto {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String cif;
    private String sector;
    private Integer employees;
    private Integer foundedYear;

    /** Contraseña en texto plano; se guarda su MD5. Obligatoria en POST, opcional en PUT/PATCH. */
    private String password;
}
