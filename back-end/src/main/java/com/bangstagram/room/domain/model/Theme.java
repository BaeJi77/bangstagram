package com.bangstagram.room.domain.model;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Room room;

    @Column(nullable = false)
    private String title;
    private String imgSrc;

    @Column(columnDefinition = "TEXT")
    private String description;
    private String genre;

    public Theme() {
    }

    @Builder
    public Theme(Long id, String title, String imgSrc, String description, String genre) {
        this.id = id;
        this.title = title;
        this.imgSrc = imgSrc;
        this.description = description;
        this.genre = genre;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void update(String title, String imgSrc, String description, String genre) {
        this.title = title;
        this.imgSrc = imgSrc;
        this.description = description;
        this.genre = genre;
    }
}
