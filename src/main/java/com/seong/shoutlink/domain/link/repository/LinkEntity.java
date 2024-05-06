package com.seong.shoutlink.domain.link.repository;

import com.seong.shoutlink.domain.common.BaseEntity;
import com.seong.shoutlink.domain.domain.Domain;
import com.seong.shoutlink.domain.link.Link;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(nullable = false)
    private String url;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_bundle_id", nullable = false)
    private LinkBundleEntity linkBundle;

    private Long domainId;

    private LinkEntity(String url, String description, LinkBundleEntity linkBundleEntity) {
        this.url = url;
        this.description = description;
        this.linkBundle = linkBundleEntity;
    }

    public static LinkEntity create(Link link, LinkBundleEntity linkBundleEntity) {
        return new LinkEntity(
            link.getUrl(),
            link.getDescription(),
            linkBundleEntity);
    }

    public Link toDomain() {
        return new Link(linkId, url, description);
    }

    public void updateDomainId(Domain domain) {
        domainId = domain.getDomainId();
    }

    public Long getLinkBundleId() {
        return linkBundle.getLinkBundleId();
    }
}
