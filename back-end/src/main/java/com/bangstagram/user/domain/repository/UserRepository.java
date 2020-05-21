package com.bangstagram.user.domain.repository;

import com.bangstagram.user.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    public User save(User user);

    public Optional<User> findByEmail(String email);

    public boolean existsByEmail(String email);
}