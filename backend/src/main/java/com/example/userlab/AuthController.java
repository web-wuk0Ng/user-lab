package com.example.userlab;
import java.util.Map;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
record LoginRequest(@NotBlank @Size(max=50) String username,
@NotBlank @Size(max=72) String password) {}
private final AuthMapper mapper; private final PasswordEncoder encoder; private final JwtService jwt;
public AuthController(AuthMapper m, PasswordEncoder e, JwtService j) { mapper=m; encoder=e; jwt=j; }
@PostMapping("/login")
public Map<String,String> login(@Valid @RequestBody LoginRequest req) {
AuthAccount a=mapper.find(req.username());
if (a == null || !Boolean.TRUE.equals(a.enabled()) || !encoder.matches(req.password(),a.passwordHash()))
throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials");
return Map.of("token",jwt.issue(a.username()));
}
}
