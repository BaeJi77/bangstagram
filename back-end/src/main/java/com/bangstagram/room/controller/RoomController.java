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
    public ResponseEntity<List<RoomResponseDto>> findAllRooms() {
        List<RoomResponseDto> responseDtoList = roomService.findAll();
        log.info("{}", responseDtoList);
        return new ResponseEntity<>(responseDtoList, HttpStatus.FOUND);
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<RoomResponseDto> findById(@PathVariable Long id) {
        RoomResponseDto responseDto = roomService.findById(id);
        log.info("{}", responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.FOUND);
    }

    @PostMapping("/rooms")
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody @Valid RoomSaveRequestDto roomSaveRequestDto) {
        RoomResponseDto createdRoom = roomService.createRoom(roomSaveRequestDto);
        log.info("{}", createdRoom);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }

    @PutMapping("/rooms/{id}")
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable Long id, @RequestBody @Valid RoomUpdateRequestDto roomUpdateRequestDto) {
        RoomResponseDto updateRoom = roomService.updateRoom(id, roomUpdateRequestDto);
        log.info("{}", updateRoom);
        return new ResponseEntity<>(updateRoom, HttpStatus.CREATED);
    }

}
