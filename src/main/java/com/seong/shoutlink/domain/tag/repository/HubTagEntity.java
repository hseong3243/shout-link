package com.seong.shoutlink.domain.tag.repository;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("hub")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubTagEntity extends TagEntity {

    private Long hubId;

    public HubTagEntity(String name, Long hubId) {
        super(name);
        this.hubId = hubId;
    }
}
