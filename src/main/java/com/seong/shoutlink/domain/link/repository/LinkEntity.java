package com.seong.shoutlink.domain.link.repository;

import com.seong.shoutlink.domain.common.BaseEntity;
import com.seong.shoutlink.domain.domain.Domain;
import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.LinkWithLinkBundle;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "link")
public class LinkEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkId;

    private String url;
    private String description;
    private Long linkBundleId;
    private Long domainId;

    private LinkEntity(String url, String description, Long linkBundleId) {
        this.url = url;
        this.description = description;
        this.linkBundleId = linkBundleId;
    }

    public static LinkEntity create(LinkWithLinkBundle linkWithLinkBundle) {
        Link link = linkWithLinkBundle.getLink();
        LinkBundle linkBundle = linkWithLinkBundle.getLinkBundle();
        return new LinkEntity(
            link.getUrl(),
            link.getDescription(),
            linkBundle.getLinkBundleId());
    }

    public Link toDomain() {
        return new Link(linkId, url, description);
    }

    public void updateDomainId(Domain domain) {
        domainId = domain.getDomainId();
    }

    public boolean isContainsIn(LinkBundle linkBundle) {
        return linkBundleId.equals(linkBundle.getLinkBundleId());
    }
}
