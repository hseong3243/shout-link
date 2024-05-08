package com.seong.shoutlink.domain.link.linkbundle.repository;

import com.seong.shoutlink.domain.common.BaseEntity;
import com.seong.shoutlink.domain.hub.repository.HubEntity;
import com.seong.shoutlink.domain.link.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.member.repository.MemberEntity;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "link_bundle")
public abstract class LinkBundleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkBundleId;

    private String description;
    private boolean isDefault;

    protected LinkBundleEntity(
        String description,
        boolean isDefault) {
        this.description = description;
        this.isDefault = isDefault;
    }

    public static LinkBundleEntity create(LinkBundle linkBundle, MemberEntity memberEntity) {
        return new MemberLinkBundleEntity(
            linkBundle.getDescription(),
            linkBundle.isDefault(),
            memberEntity
        );
    }

    public static LinkBundleEntity create(LinkBundle linkBundle, HubEntity hubEntity) {
        return new HubLinkBundleEntity(
            linkBundle.getDescription(),
            linkBundle.isDefault(),
            hubEntity
        );
    }

    public LinkBundle toDomain() {
        return new LinkBundle(
            linkBundleId,
            description,
            isDefault
        );
    }
}
