package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.RoomSaveRequestDto;
import com.bangstagram.room.controller.dto.RoomResponseDto;
import com.bangstagram.room.controller.dto.RoomUpdateRequestDto;
import com.bangstagram.room.service.RoomService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/rooms/{id}")
    public RoomResponseDto findById(@PathVariable Long id) {
        return roomService.findById(id);
    }

    @PostMapping("/rooms")
    public Long createRoom(@RequestBody RoomSaveRequestDto roomSaveRequestDto) {
        return roomService.createRoom(roomSaveRequestDto);
    }

    @PutMapping("/rooms/{id}")
    public Long updateRoom(@PathVariable Long id, @RequestBody RoomUpdateRequestDto roomUpdateRequestDto) {
        return roomService.updateRoom(id, roomUpdateRequestDto);
    }

}
