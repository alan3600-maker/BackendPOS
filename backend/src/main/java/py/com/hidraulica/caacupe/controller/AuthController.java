package py.com.hidraulica.caacupe.controller;

import py.com.hidraulica.caacupe.dto.auth.AuthResponse;
import py.com.hidraulica.caacupe.dto.auth.LoginRequest;
import py.com.hidraulica.caacupe.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authManager;
  private final JwtService jwtService;

  public AuthController(AuthenticationManager authManager, JwtService jwtService) {
    this.authManager = authManager;
    this.jwtService = jwtService;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
    Authentication auth = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
    );

    Set<String> roles = auth.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .filter(a -> a.startsWith("ROLE_"))
        .map(a -> a.substring("ROLE_".length()))
        .collect(Collectors.toSet());

    Set<String> permisos = auth.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .filter(a -> a.startsWith("PERM_"))
        .map(a -> a.substring("PERM_".length()))
        .collect(Collectors.toSet());

    var claims = new HashMap<String, Object>();
    claims.put("roles", roles);
    claims.put("permisos", permisos);

    String token = jwtService.generateToken(auth.getName(), claims);
    return ResponseEntity.ok(new AuthResponse(token, auth.getName(), roles, permisos));
  }
}
