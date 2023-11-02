package javasecurity.securityJWT.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Must be unique email and try to find unique email
    Optional<User> findfindByEmail(String email);
}
