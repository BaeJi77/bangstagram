package com.bangstagram.room.domain.repository;

import com.bangstagram.room.domain.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByThemeId(Long themeId);
}
