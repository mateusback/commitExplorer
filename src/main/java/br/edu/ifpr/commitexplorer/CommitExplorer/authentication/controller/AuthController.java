package br.edu.ifpr.commitexplorer.CommitExplorer.authentication.controller;

import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.BaseResponse;
import br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos.ResponseBuilder;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.dto.LoginRequest;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.dto.LoginResponse;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.dto.SignupRequest;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.dto.CurrentUserResponse;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.dto.PromoteProfessorRequest;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.jwt.JwtTokenService;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.model.Role;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.model.User;
import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    public ResponseEntity<BaseResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest req) {
        Authentication authentication = null;

        try {
            authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.email().toLowerCase(), req.password())
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.badRequest().body(ResponseBuilder.error("Credenciais inválidas"));
        } catch (DisabledException ex) {
            return ResponseEntity.status(403).body(ResponseBuilder.error("Usuário desabilitado"));
        } catch (LockedException ex) {
            return ResponseEntity.status(403).body(ResponseBuilder.error("Usuário bloqueado"));
        } catch (AccountExpiredException ex) {
            return ResponseEntity.status(403).body(ResponseBuilder.error("Conta expirada"));
        } catch (CredentialsExpiredException ex) {
            return ResponseEntity.status(403).body(ResponseBuilder.error("Credenciais expiradas"));
        }

        var principal = authentication.getName();
        String token = jwt.generateToken(principal, Map.of("scope", "api"));
        return ResponseEntity.ok(ResponseBuilder.success(LoginResponse.bearer(token), "Login realizado com sucesso"));
    }

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<LoginResponse>> signup(@RequestBody @Valid SignupRequest req) {
        String email = req.email().toLowerCase();
        if (users.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(ResponseBuilder.error("Email já cadastrado"));
        }

        var user = new User(req.name(), email, encoder.encode(req.password()), Set.of(Role.USER));
        users.save(user);

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, req.password())
        );

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(500).body(ResponseBuilder.error("Erro ao autenticar usuário recém-criado"));
        }

        String subject = authentication.getName();
        String token = jwt.generateToken(subject, Map.of("scope", "api"));

        return ResponseEntity.ok(ResponseBuilder.success(LoginResponse.bearer(token), "Usuário registrado com sucesso"));
    }

    @GetMapping("/me")
    public ResponseEntity<BaseResponse<CurrentUserResponse>> me(@AuthenticationPrincipal UserDetails me) {
        if (me == null) {
            return ResponseEntity.status(401).body(ResponseBuilder.error("Não autenticado"));
        }
        var user = users.findByEmail(me.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(ResponseBuilder.error("Usuário não encontrado"));
        }
        var roles = user.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        var dto = new CurrentUserResponse(user.getId(), user.getName(), user.getEmail(), roles);
        return ResponseEntity.ok(ResponseBuilder.success(dto, "Usuário atual"));
    }

    @PostMapping("/professors")
    @PreAuthorize("hasAuthority('PROFESSOR')")
    public ResponseEntity<BaseResponse<Object>> promoteProfessor(@RequestBody PromoteProfessorRequest req) {
        if (req == null || req.email() == null || req.email().isBlank()) {
            return ResponseEntity.ok(ResponseBuilder.success("Email inválido"));
        }
        String email = req.email().toLowerCase(Locale.ROOT);
        var userOpt = users.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok(ResponseBuilder.success("Usuário não encontrado"));
        }
        var user = userOpt.get();
        if (!user.getRoles().contains(Role.PROFESSOR)) {
            user.getRoles().add(Role.PROFESSOR);
            users.save(user);
            return ResponseEntity.ok(ResponseBuilder.success("Usuário promovido a professor"));
        }
        return ResponseEntity.ok(ResponseBuilder.success("Usuário já é professor"));
    }
}
