package cz.catparadise.controller;

import cz.catparadise.dto.LoginRequest;
import cz.catparadise.dto.RegistrationRequest;
import cz.catparadise.model.User;
import cz.catparadise.repository.UserRepository;
import cz.catparadise.security.JwtUtil;
import cz.catparadise.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, UserRepository userRepository,
                          PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Registrace
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Email already exists"));
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Phone number already exists"));
        }

        String hashedPassword = null;
        if (request.getPasswordHash() != null && !request.getPasswordHash().isEmpty()) {
            hashedPassword = passwordEncoder.encode(request.getPasswordHash());
        }

        User user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                hashedPassword,
                request.getPhoneNumber(),
                LocalDateTime.now()
        );
        user.setRole("USER");

        try {
            User saved = userService.saveUser(user);
            String token = jwtUtil.generationToken(saved.getEmail(), saved.getRole());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("token", token, "role", saved.getRole()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Data conflict"));
        }

    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPasswordHash(), user.getPasswordHash())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
        String token = jwtUtil.generationToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(Map.of("token", token, "role", user.getRole()));
    }
}