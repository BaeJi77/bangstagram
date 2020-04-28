package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.RoomResponseDto;
import com.bangstagram.room.service.RoomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/rooms")
    public List<RoomResponseDto> getAllRooms() {
        return roomService.getAll();
    }
}
