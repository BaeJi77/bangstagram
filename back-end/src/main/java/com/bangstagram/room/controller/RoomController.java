package com.bangstagram.room.controller;

import com.bangstagram.room.controller.dto.request.RoomSaveRequestDto;
import com.bangstagram.room.controller.dto.request.RoomUpdateRequestDto;
import com.bangstagram.room.controller.dto.request.ThemeSaveRequestDto;
import com.bangstagram.room.controller.dto.request.ThemeUpdateRequestDto;
import com.bangstagram.room.controller.dto.response.RoomResponseDto;
import com.bangstagram.room.service.RoomService;
import lombok.extern.slf4j.Slf4j;
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
        log.info("[create Room] {}", roomSaveRequestDto);
        return roomService.createRoom(roomSaveRequestDto);
    }

    @PutMapping("/rooms/{id}")
    public RoomResponseDto updateRoom(@PathVariable Long id, @RequestBody @Valid RoomUpdateRequestDto roomUpdateRequestDto) {
        log.info("[update Room] id={}, {}", id, roomUpdateRequestDto);
        return roomService.updateRoom(id, roomUpdateRequestDto);
    }

    @GetMapping("/rooms/search")
    public List<RoomResponseDto> findRoomByRegion(@RequestParam(value = "region") String region) {
        log.info("[search Room By Region] {}", region);
        return roomService.findRoomByRegion(region);
    }

    @DeleteMapping("/rooms/{id}")
    public void deleteRoom(@PathVariable Long id) {
        log.info("[delete Room] id={}", id);
        roomService.deleteRoom(id);
    }

    @PostMapping("/rooms/themes/{id}")
    public RoomResponseDto createThemes(@PathVariable Long id, @RequestBody @Valid List<ThemeSaveRequestDto> requestDtos) {
        log.info("[create room themes] id={}, {}", id, requestDtos);
        return roomService.addThemes(id, requestDtos);
    }

    @PutMapping("/rooms/themes/{id}")
    public RoomResponseDto updateThemes(@PathVariable Long id, @RequestBody @Valid List<ThemeUpdateRequestDto> requestDtos) {
        log.info("[update room themes] id={}, {}", id, requestDtos);
        return roomService.updateThemes(id, requestDtos);
    }
}
