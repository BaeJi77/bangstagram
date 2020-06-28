package com.bangstagram.room.domain.repository;

import com.bangstagram.room.domain.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    List<Theme> findByRoomId(Long roomId);
}
