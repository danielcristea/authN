package tech.ing.authorization.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.ing.authorization.model.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByUsername(String username);
}
