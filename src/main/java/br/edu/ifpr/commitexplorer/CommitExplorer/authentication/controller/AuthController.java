package br.edu.ifpr.commitexplorer.CommitExplorer.authentication.controller;

import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.dto.LoginRequest;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.dto.LoginResponse;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.dto.SignupRequest;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.jwt.JwtTokenService;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.model.Role;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.model.User;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtTokenService jwt;
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthController(AuthenticationManager authManager,
                          JwtTokenService jwt,
                          UserRepository users,
                          PasswordEncoder encoder) {
        this.authManager = authManager;
        this.jwt = jwt;
        this.users = users;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest req) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email().toLowerCase(), req.password())
        );

        var principal = authentication.getName();
        String token = jwt.generateToken(principal, Map.of("scope", "api"));
        return ResponseEntity.ok(LoginResponse.bearer(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest req) {
        String email = req.email().toLowerCase();
        if (users.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado");
        }

        var user = new User(req.name(), email, encoder.encode(req.password()), Set.of(Role.USER));
        users.save(user);

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, req.password())
        );

        String subject = authentication.getName();
        String token = jwt.generateToken(subject, Map.of("scope", "api"));

        return ResponseEntity.ok(LoginResponse.bearer(token));
    }
}
