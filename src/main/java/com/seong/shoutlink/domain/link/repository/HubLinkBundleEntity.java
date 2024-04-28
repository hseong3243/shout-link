package com.seong.shoutlink.domain.link.repository;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue("hub")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hub_link_bundle")
public class HubLinkBundleEntity extends LinkBundleEntity {

    private Long hubId;

    public HubLinkBundleEntity(String description, boolean isDefault, Long hubId) {
        super(description, isDefault);
        this.hubId = hubId;
    }
}
