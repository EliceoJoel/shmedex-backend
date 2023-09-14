package com.eherbas.shmedex.repository;

import java.util.Optional;

import com.eherbas.shmedex.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);
}
