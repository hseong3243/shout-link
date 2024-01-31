package com.seong.shoutlink.domain.linkbundle.repository;

import com.seong.shoutlink.domain.linkbundle.LinkBundle;
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
public class LinkBundleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkBundleId;

    private String description;
    private boolean isDefault;
    private Long memberId;

    private LinkBundleEntity(
        Long linkBundleId,
        String description,
        boolean isDefault,
        Long memberId) {
        this.linkBundleId = linkBundleId;
        this.description = description;
        this.isDefault = isDefault;
        this.memberId = memberId;
    }

    public static LinkBundleEntity create(LinkBundle linkBundle) {
        return new LinkBundleEntity(
            linkBundle.getLinkBundleId(),
            linkBundle.getDescription(),
            linkBundle.isDefault(),
            linkBundle.getMemberId());
    }

    public LinkBundle toDomain() {
        return new LinkBundle(
            linkBundleId,
            description,
            isDefault,
            memberId
        );
    }
}
