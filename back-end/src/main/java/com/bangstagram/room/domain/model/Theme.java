package com.bangstagram.room.domain.model;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "THEME_ID")
    private Long id;

    @Column(nullable = false)
    private String title;
    private String imgSrc;

    @Column(columnDefinition = "TEXT")
    private String description;
    private String genre;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ROOM_ID")
    private Room room;

    public Theme() {
    }

    @Builder
    public Theme(Long id, String title, String imgSrc, String description, String genre, Room room) {
        this.id = id;
        this.title = title;
        this.imgSrc = imgSrc;
        this.description = description;
        this.genre = genre;
        this.room = room;
    }

    public void setRoom(Room room) {
        if(this.room != null) {
            this.room.getThemes().remove(this);
        }
        this.room = room;
        if(!room.getThemes().contains(this)) {
            room.getThemes().add(this);
        }
    }
}
