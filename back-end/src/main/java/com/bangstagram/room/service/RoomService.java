package com.bangstagram.room.service;

import com.bangstagram.room.controller.dto.RoomSaveRequestDto;
import com.bangstagram.room.controller.dto.RoomResponseDto;
import com.bangstagram.room.controller.dto.RoomUpdateRequestDto;
import com.bangstagram.room.domain.model.Room;
import com.bangstagram.room.domain.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public List<RoomResponseDto> findAll() {
        return roomRepository.findAll().stream()
                .map(RoomResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomResponseDto findById(Long id) {
        Room room = findRoomById(id);
        return new RoomResponseDto(room);
    }

    public RoomResponseDto createRoom(RoomSaveRequestDto requestDto) {
        Room room = roomRepository.save(requestDto.toEntity());

        return RoomResponseDto.builder()
                .id(room.getId())
                .title(room.getTitle())
                .address(room.getAddress())
                .link(room.getLink())
                .phone(room.getPhone())
                .description(room.getDescription())
                .build();
    }

    @Transactional
    public RoomResponseDto updateRoom(Long id, RoomUpdateRequestDto roomUpdateRequestDto) {
        Room room = findRoomById(id);
        room.update(roomUpdateRequestDto.getTitle(), roomUpdateRequestDto.getLink(),
                roomUpdateRequestDto.getPhone(), roomUpdateRequestDto.getAddress(), roomUpdateRequestDto.getDescription());

        return RoomResponseDto.builder()
                .id(room.getId())
                .title(room.getTitle())
                .address(room.getAddress())
                .link(room.getLink())
                .phone(room.getPhone())
                .description(room.getDescription())
                .build();
    }

    private Room findRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(id + " 해당 정보가 없습니다."));
    }
}
