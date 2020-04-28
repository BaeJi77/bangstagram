package com.bangstagram.timeline.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
public class Timeline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String body;

    private LocalDateTime createdAt;

    private Long userId;

    private Long roomId;

    public Timeline() {
    }

    @Builder
    public Timeline(String title, String body, Long userId, Long roomId) {
        this.title = title;
        this.body = body;
        this.createdAt = LocalDateTime.now();
        this.userId = userId;
        this.roomId = roomId;
    }
}