package com.seong.shoutlink.domain.tag.repository;

import com.seong.shoutlink.domain.hub.repository.HubEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("hub")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubTagEntity extends TagEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id")
    private HubEntity hub;

    public HubTagEntity(String name, HubEntity hubEntity) {
        super(name);
        this.hub = hubEntity;
    }

    public Long getHubId() {
        return hub.getHubId();
    }
}
