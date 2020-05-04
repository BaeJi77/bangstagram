package com.bangstagram.user.domain.repository;

import com.bangstagram.user.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    public User save(User user);

    public User findByEmail(String email);

    public boolean existsByEmail(String email);
}