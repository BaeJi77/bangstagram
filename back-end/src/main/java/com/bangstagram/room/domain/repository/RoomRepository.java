package com.bangstagram.room.domain.repository;

import com.bangstagram.room.domain.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
