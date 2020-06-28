package com.bangstagram.timeline.domain.model;

import com.bangstagram.common.model.CommonEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * author: Ji-Hoon Bae
 * Date: 2020.05.21
 */

@Entity
@Getter
public class TimelineComment extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    private Long userId;

    private Long timelineId;

    public TimelineComment() {
    }

    @Builder
    public TimelineComment(String comment, Long userId, Long timelineId) {
        this.comment = comment;
        this.userId = userId;
        this.timelineId = timelineId;
    }

    public void update(String comment) {
        this.comment = comment;
    }
}
