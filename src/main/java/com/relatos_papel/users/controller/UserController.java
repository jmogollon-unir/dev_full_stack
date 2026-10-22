package com.relatos_papel.users.controller;

import com.relatos_papel.users.controller.dto.LoginRequest;
import com.relatos_papel.users.controller.dto.SaveUserDto;
import com.relatos_papel.users.controller.dto.UserProfileDto;
import com.relatos_papel.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UsersService usersService;

    /** Login con correo + contraseña. Devuelve el perfil del usuario (sin tokens). 401 si las credenciales no son válidas. */
    @PostMapping("/login")
    public ResponseEntity<UserProfileDto> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(usersService.login(request));
    }

    @GetMapping
    public ResponseEntity<List<UserProfileDto>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String cif) {
        return ResponseEntity.ok(usersService.search(name, email, cif));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(usersService.getById(id));
    }

    @GetMapping("/cif/{cif}")
    public ResponseEntity<UserProfileDto> getUserByCif(@PathVariable String cif) {
        return ResponseEntity.ok(usersService.getByCif(cif));
    }

    @PostMapping
    public ResponseEntity<UserProfileDto> createUser(@RequestBody SaveUserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDto> replaceUser(@PathVariable Integer id, @RequestBody SaveUserDto dto) {
        return ResponseEntity.ok(usersService.replace(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserProfileDto> updateUser(@PathVariable Integer id, @RequestBody SaveUserDto dto) {
        return ResponseEntity.ok(usersService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        usersService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
