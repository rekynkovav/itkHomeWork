package ru.suveren.task7.lesson2.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.suveren.task7.lesson2.model.User;
import ru.suveren.task7.lesson2.service.UserService;
import ru.suveren.task7.lesson2.utils.JwtUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        try {
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

            if (!user.isAccountNonLocked()) {
                logger.warn("Account is locked: {}", username);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Account is locked. Contact administrator."));
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            userService.resetFailedAttempts(username);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtils.generateToken(userDetails);
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), userDetails);

            logger.info("Successful login for user: {}", username);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("refreshToken", refreshToken);
            response.put("username", username);
            response.put("role", user.getRole().name());

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            userService.incrementFailedAttempts(username);
            logger.warn("Failed login attempt for user: {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid credentials"));
        } catch (LockedException e) {
            logger.warn("Locked account attempt for user: {}", username);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Account is locked"));
        } catch (Exception e) {
            logger.error("Login error for user {}: {}", username, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Login failed"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> refreshRequest) {
        String refreshToken = refreshRequest.get("refreshToken");

        try {
            String username = jwtUtils.extractUsername(refreshToken);
            UserDetails userDetails = userService.loadUserByUsername(username);

            if (jwtUtils.isTokenValid(refreshToken, userDetails)) {
                String newToken = jwtUtils.generateToken(userDetails);
                Map<String, String> response = new HashMap<>();
                response.put("token", newToken);
                logger.info("Token refreshed for user: {}", username);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid refresh token"));
            }
        } catch (Exception e) {
            logger.error("Refresh token error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid refresh token"));
        }
    }

    @PostMapping("/unlock/{username}")
    public ResponseEntity<?> unlockAccount(@PathVariable String username) {
        try {
            userService.unlockAccount(username);
            logger.info("Account unlocked for user: {}", username);
            return ResponseEntity.ok(Map.of("message", "Account unlocked successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }
    }
}