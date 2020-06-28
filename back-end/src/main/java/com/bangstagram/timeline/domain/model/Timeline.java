package com.bangstagram.timeline.domain.model;

import com.bangstagram.common.model.CommonEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * author: Ji-Hoon Bae
 * Date: 2020.04.28
 */

@Entity
@Getter
public class Timeline extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String body;

    private Long userId;

    private Long roomId;

    @OneToMany
    private List<TimelineComment> timelineComments = new ArrayList<>();

    public Timeline() {
    }

    @Builder
    public Timeline(String title, String body, Long userId, Long roomId) {
        this.title = title;
        this.body = body;
        this.userId = userId;
        this.roomId = roomId;
    }

    public void update(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public void addTimelineComment(TimelineComment timelineComment) {
        this.timelineComments.add(timelineComment);
    }
}