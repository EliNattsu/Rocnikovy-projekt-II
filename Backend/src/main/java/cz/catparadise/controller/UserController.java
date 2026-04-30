package cz.catparadise.controller;

import cz.catparadise.model.User;
import cz.catparadise.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Přidání nového uživatele
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // Výpis všech uživatelů
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Najdi uživatele podle ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Vyhledávání uživatele podle e-mailu
    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Smazání uživatele podle ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Integer id,
            @RequestBody User updatedUser
    ) {
        return userService.getUserById(id).map(user -> {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            userService.saveUser(user);
            return ResponseEntity.ok(user);
        }).orElse(ResponseEntity.notFound().build());
    }
}