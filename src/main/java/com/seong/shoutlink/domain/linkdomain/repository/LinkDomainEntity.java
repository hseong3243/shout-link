package com.seong.shoutlink.domain.linkdomain.repository;

import com.seong.shoutlink.domain.common.BaseEntity;
import com.seong.shoutlink.domain.linkdomain.LinkDomain;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "domain")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LinkDomainEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long domainId;
    private String rootDomain;

    public LinkDomainEntity(String rootDomain) {
        this(null, rootDomain);
    }

    public LinkDomainEntity(Long domainId, String rootDomain) {
        this.domainId = domainId;
        this.rootDomain = rootDomain;
    }

    public static LinkDomainEntity create(LinkDomain linkDomain) {
        return new LinkDomainEntity(linkDomain.getRootDomain());
    }

    public LinkDomain toDomain() {
        return new LinkDomain(domainId, rootDomain);
    }
}
