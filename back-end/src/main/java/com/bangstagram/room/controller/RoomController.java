package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.RoomSaveRequestDto;
import com.bangstagram.room.controller.dto.RoomResponseDto;
import com.bangstagram.room.controller.dto.RoomUpdateRequestDto;
import com.bangstagram.room.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/rooms")
    public List<RoomResponseDto> findAllRooms() {
        return roomService.findAll();
    }

    @GetMapping("/rooms/{id}")
    public RoomResponseDto findById(@PathVariable Long id) {
        return roomService.findById(id);
    }

    @PostMapping("/rooms")
    public RoomResponseDto createRoom(@RequestBody @Valid RoomSaveRequestDto roomSaveRequestDto) {
        log.info("{}", roomSaveRequestDto);
        return roomService.createRoom(roomSaveRequestDto);
    }

    @PutMapping("/rooms/{id}")
    public RoomResponseDto updateRoom(@PathVariable Long id, @RequestBody @Valid RoomUpdateRequestDto roomUpdateRequestDto) {
        log.info("{}, {}", id, roomUpdateRequestDto);
        return roomService.updateRoom(id, roomUpdateRequestDto);
    }

}
