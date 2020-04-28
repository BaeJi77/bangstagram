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

    @Column
    private String link;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String phone;

    @Column
    private String address;

    public Room() {}

    @Builder
    public Room(String title, String link, String description, String phone, String address) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.phone = phone;
        this.address = address;
    }

}
