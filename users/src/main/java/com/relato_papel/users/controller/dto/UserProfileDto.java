package com.relato_papel.users.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Formato alineado con el front (Profile): userId + objeto company.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private Integer userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String cif;
    private String sector;
    private Integer employees;
    private Integer foundedYear;
    private Company company;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Company {
        private String name;
        private String email;
        private String phone;
        private String address;
        private String sector;
        private Integer employees;
        private Integer foundedYear;
    }
}
