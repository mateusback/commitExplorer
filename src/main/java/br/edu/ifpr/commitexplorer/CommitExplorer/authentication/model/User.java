package br.edu.ifpr.commitexplorer.CommitExplorer.authentication.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String name;

    @Column(nullable = false) private String email;

    @Column(nullable = false) private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private boolean enabled = true;

    protected User() { }

    public User(String name, String email, String password, Set<Role> roles) {
        this.name = name;
        this.email = email.toLowerCase(Locale.ROOT);
        this.password = password;
        this.roles = roles != null ? roles : Set.of(Role.USER);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r -> (GrantedAuthority) r::name).collect(Collectors.toSet());
    }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }

    public Set<Role> getRoles() { return roles; }
    public void setPassword(String password) { this.password = password; }
}