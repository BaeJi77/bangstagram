package com.bangstagram.user.domain.repository;

import com.bangstagram.user.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.08
 */

public interface UserRepository extends JpaRepository<User,Long> {
    User save(User user);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}