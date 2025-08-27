package br.edu.ifpr.commitexplorer.CommitExplorer.authentication.repository;

import br.edu.ifpr.commitexplorer.CommitExplorer.authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}