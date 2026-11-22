package com.example.userlab;
import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/users")
public class UserController {
private final UserService service;
public UserController(UserService service) { this.service = service; }
@PreAuthorize("hasAuthority('user:read')")
@GetMapping public List<User> list() { return service.list(); }
@PreAuthorize("hasAuthority('user:read')")
@GetMapping("/{id}") public User get(@PathVariable Long id) { return service.get(id); }
@PreAuthorize("hasAuthority('user:write')")
@PostMapping public ResponseEntity<User> create(@Valid @RequestBody User user) {
User created = service.create(user);
return ResponseEntity.created(URI.create("/api/users/" + created.getId())).body(created);
}
@PreAuthorize("hasAuthority('user:write')")
@PutMapping("/{id}") public User update(@PathVariable Long id, @Valid @RequestBody User user) { return service.update(id, user); }
@PreAuthorize("hasAuthority('user:write')")
@DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) {
service.delete(id); return ResponseEntity.noContent().build();
}
}
