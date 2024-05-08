package com.seong.shoutlink.domain.link.linkbundle.repository;

import com.seong.shoutlink.domain.hub.repository.HubEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id")
    private HubEntity hub;

    public HubLinkBundleEntity(String description, boolean isDefault, HubEntity hubEntity) {
        super(description, isDefault);
        this.hub = hubEntity;
    }
}
