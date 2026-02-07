package twilio_spa_fetch_backend.controller;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import twilio_spa_fetch_backend.dto.LoginRequestDTO;
import twilio_spa_fetch_backend.dto.LoginResponseDTO;
import twilio_spa_fetch_backend.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            LoginResponseDTO response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid credentials", "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?>  validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer", "");
            boolean isValid = authService.validateToken(token);
            if (isValid) {
                String accountSid = authService.getAccountSidFromToken(token);
                return ResponseEntity.ok(Map.of(
                        "valid", true,
                        "accountSid", accountSid
                ));
            } else {
                return ResponseEntity.status(401).body(Map.of("valid", false));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("valid", false));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

}
