package com.bangstagram.room.domain.model;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String link;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String phone;

    private String address;

    public Room() {
    }

    @Builder
    public Room(Long id, String title, String link, String description, String phone, String address) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.phone = phone;
        this.address = address;
    }

    public void update(String title, String link, String phone, String address, String description) {
        this.title = title;
        this.link = link;
        this.phone = phone;
        this.address = address;
        this.description = description;
    }
}
