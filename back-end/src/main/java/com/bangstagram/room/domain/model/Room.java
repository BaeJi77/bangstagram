package com.bangstagram.room.domain.model;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROOM_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    private String link;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String phone;

    private String address;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Theme> themes = new ArrayList<>();

    public Room() {
    }

    @Builder
    public Room(Long id, String title, String link, String description, String phone, String address, List<Theme> themes) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.phone = phone;
        this.address = address;
        this.themes.addAll(themes);
    }

    public void update(String title, String link, String phone, String address, String description, List<Theme> themes) {
        this.title = title;
        this.link = link;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.themes = themes;
    }

    public void addTheme(Theme theme) {
        if(this.themes == null) {
            this.themes = new ArrayList<>();
        }
        this.themes.add(theme);
        if(theme.getRoom() != this) {
            theme.setRoom(this);
        }
    }
}
