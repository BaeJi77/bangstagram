package com.bangstagram.room.service;

import com.bangstagram.room.controller.dto.RoomResponseDto;
import com.bangstagram.room.crowler.RoomCrawler;
import com.bangstagram.room.domain.model.Room;
import com.bangstagram.room.domain.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomCrawler roomCrawler;

    public RoomService(RoomRepository roomRepository, RoomCrawler roomCrawler) {
        this.roomRepository = roomRepository;
        this.roomCrawler = roomCrawler;
    }

    public List<RoomResponseDto> getAll() {

        List<Room> rooms = roomRepository.findAll();
        if(rooms.isEmpty()) {
            rooms = Optional.ofNullable(roomCrawler.getList()).orElseThrow(() ->
                    new IllegalArgumentException());
            roomRepository.saveAll(rooms);
        }
        List<RoomResponseDto> roomResponseDtos = new ArrayList<>();
        rooms.forEach(room -> {
            roomResponseDtos.add(new RoomResponseDto(room));
        });

        return roomResponseDtos;

    }

    public RoomResponseDto findById(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException(id+ " 해당 정보가 없습니다.")
        );
        return new RoomResponseDto(room);
    }

}
