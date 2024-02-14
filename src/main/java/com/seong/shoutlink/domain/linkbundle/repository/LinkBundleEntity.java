package com.seong.shoutlink.domain.linkbundle.repository;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.linkbundle.HubLinkBundle;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.MemberLinkBundle;
import com.seong.shoutlink.domain.member.Member;
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
public abstract class LinkBundleEntity {

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

    public static LinkBundleEntity create(MemberLinkBundle memberLinkBundle) {
        LinkBundle linkBundle = memberLinkBundle.getLinkBundle();
        Member member = memberLinkBundle.getMember();
        return new MemberLinkBundleEntity(
            linkBundle.getDescription(),
            linkBundle.isDefault(),
            member.getMemberId()
        );
    }

    public static LinkBundleEntity create(HubLinkBundle hubLinkBundle) {
        LinkBundle linkBundle = hubLinkBundle.getLinkBundle();
        Hub hub = hubLinkBundle.getHub();
        return new HubLinkBundleEntity(
            linkBundle.getDescription(),
            linkBundle.isDefault(),
            hub.getHubId()
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
