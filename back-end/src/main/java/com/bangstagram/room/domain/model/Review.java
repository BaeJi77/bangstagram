package com.bangstagram.room.domain.model;

import com.bangstagram.common.model.CommonEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Review extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    private Theme theme;

    @Column(columnDefinition = "TEXT")
    private String content;
    private double score;
    private String level;
    private boolean success;
    private int leftTime;
    private int hintCount;

    public Review() {
    }

    @Builder
    public Review(Long id, Long userId, String content, double score, String level, boolean success, int leftTime, int hintCount) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.score = score;
        this.level = level;
        this.success = success;
        this.leftTime = leftTime;
        this.hintCount = hintCount;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public void update(String content, double score, String level, boolean success, int leftTime, int hintCount) {
        this.content = content;
        this.score = score;
        this.level = level;
        this.success = success;
        this.leftTime = leftTime;
        this.hintCount = hintCount;
    }
}
