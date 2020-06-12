package com.bangstagram.room.domain.repository;

import com.bangstagram.room.domain.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
}
