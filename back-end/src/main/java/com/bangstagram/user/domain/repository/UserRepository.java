package com.bangstagram.user.domain.repository;

import com.bangstagram.user.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public User save(User user);

    public Optional<User> findByEmail(String email);
}
