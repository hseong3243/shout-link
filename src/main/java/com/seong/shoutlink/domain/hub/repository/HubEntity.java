package com.seong.shoutlink.domain.hub.repository;

import com.seong.shoutlink.domain.hub.Hub;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hubId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    private HubEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static HubEntity create(Hub hub) {
        return new HubEntity(hub.getName(), hub.getDescription());
    }
}
