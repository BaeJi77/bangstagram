package com.bangstagram.timeline.domain.model;

import com.bangstagram.auditing.TimeAuditorEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
public class Timeline extends TimeAuditorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String body;

    private Long userId;

    private Long roomId;

    public Timeline() {
    }

    @Builder
    public Timeline(String title, String body, Long userId, Long roomId) {
        this.title = title;
        this.body = body;
        this.userId = userId;
        this.roomId = roomId;
    }
}