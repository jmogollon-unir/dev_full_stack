package com.relatos_papel.users.service;

import com.relatos_papel.users.controller.dto.LoginRequest;
import com.relatos_papel.users.controller.dto.SaveUserDto;
import com.relatos_papel.users.controller.dto.UserProfileDto;
import com.relatos_papel.users.exception.DuplicateResourceException;
import com.relatos_papel.users.exception.InvalidCredentialsException;
import com.relatos_papel.users.exception.InvalidRequestException;
import com.relatos_papel.users.exception.ResourceNotFoundException;
import com.relatos_papel.users.repository.users.UserJpaRepository;
import com.relatos_papel.users.repository.users.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UserJpaRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserProfileDto> search(String name, String email, String cif) {
        List<User> users;
        if (cif != null && !cif.isBlank()) {
            users = userRepository.findByCif(cif).map(List::of).orElse(List.of());
        } else if (email != null && !email.isBlank()) {
            users = userRepository.findByEmail(email).map(List::of).orElse(List.of());
        } else if (name != null && !name.isBlank()) {
            users = userRepository.findByNameContainingIgnoreCase(name);
        } else {
            users = userRepository.findAll();
        }
        return users.stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public UserProfileDto getById(Integer id) {
        return toDto(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public UserProfileDto getByCif(String cif) {
        return userRepository.findByCif(cif)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con CIF " + cif));
    }

    /** Valida correo + contraseña y devuelve el perfil (sin tokens ni sesión). */
    @Transactional(readOnly = true)
    public UserProfileDto login(LoginRequest request) {
        if (request == null || isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            throw new InvalidRequestException("El correo (username) y la contraseña (password) son obligatorios.");
        }
        String username = request.getUsername().trim();
        // El login es por correo; por compatibilidad también se acepta el CIF de la empresa.
        User user = userRepository.findByEmail(username)
                .or(() -> userRepository.findByCif(username))
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales incorrectas"));

        String received = request.getPassword();
        // Se acepta el MD5 ya calculado (front) o la contraseña en texto plano (Postman/curl).
        boolean matches = user.getPassword().equalsIgnoreCase(received)
                || user.getPassword().equalsIgnoreCase(md5(received));
        if (!matches) {
            throw new InvalidCredentialsException("Credenciales incorrectas");
        }
        return toDto(user);
    }

    @Transactional
    public UserProfileDto create(SaveUserDto dto) {
        if (isBlank(dto.getName()) || isBlank(dto.getEmail()) || isBlank(dto.getPassword())) {
            throw new InvalidRequestException("Los campos name, email y password son obligatorios.");
        }
        checkUnique(dto, null);

        User user = new User();
        apply(user, dto, true);
        return toDto(userRepository.save(user));
    }

    @Transactional
    public UserProfileDto replace(Integer id, SaveUserDto dto) {
        if (isBlank(dto.getName()) || isBlank(dto.getEmail())) {
            throw new InvalidRequestException("Los campos name y email son obligatorios.");
        }
        User user = findOrThrow(id);
        checkUnique(dto, id);
        apply(user, dto, true);
        return toDto(userRepository.save(user));
    }

    @Transactional
    public UserProfileDto update(Integer id, SaveUserDto dto) {
        User user = findOrThrow(id);
        checkUnique(dto, id);
        apply(user, dto, false);
        return toDto(userRepository.save(user));
    }

    @Transactional
    public void delete(Integer id) {
        userRepository.delete(findOrThrow(id));
    }

    private User findOrThrow(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con ID " + id));
    }

    private void checkUnique(SaveUserDto dto, Integer currentId) {
        if (!isBlank(dto.getEmail())) {
            userRepository.findByEmail(dto.getEmail())
                    .filter(u -> !u.getId().equals(currentId))
                    .ifPresent(u -> {
                        throw new DuplicateResourceException("Ya existe un usuario con el email " + dto.getEmail());
                    });
        }
        if (!isBlank(dto.getCif())) {
            userRepository.findByCif(dto.getCif())
                    .filter(u -> !u.getId().equals(currentId))
                    .ifPresent(u -> {
                        throw new DuplicateResourceException("Ya existe un usuario con el CIF " + dto.getCif());
                    });
        }
    }

    /** full=true copia todos los campos (POST/PUT); full=false solo los no nulos (PATCH). */
    private void apply(User user, SaveUserDto dto, boolean full) {
        if (full || dto.getName() != null) user.setName(dto.getName());
        if (full || dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (full || dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (full || dto.getAddress() != null) user.setAddress(dto.getAddress());
        if (full || dto.getCif() != null) user.setCif(isBlank(dto.getCif()) ? null : dto.getCif());
        if (full || dto.getSector() != null) user.setSector(dto.getSector());
        if (full || dto.getEmployees() != null) user.setEmployees(dto.getEmployees());
        if (full || dto.getFoundedYear() != null) user.setFoundedYear(dto.getFoundedYear());
        if (!isBlank(dto.getPassword())) user.setPassword(md5(dto.getPassword()));
    }

    private UserProfileDto toDto(User user) {
        return UserProfileDto.builder()
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
    }

    private static String md5(String value) {
        return DigestUtils.md5DigestAsHex(value.getBytes(StandardCharsets.UTF_8));
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
