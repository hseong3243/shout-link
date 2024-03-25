package com.seong.shoutlink.domain.domain.repository;

import com.seong.shoutlink.domain.common.BaseEntity;
import com.seong.shoutlink.domain.domain.Domain;
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
public class DomainEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long domainId;
    private String rootDomain;

    public DomainEntity(String rootDomain) {
        this(null, rootDomain);
    }

    public DomainEntity(Long domainId, String rootDomain) {
        this.domainId = domainId;
        this.rootDomain = rootDomain;
    }

    public static DomainEntity create(Domain domain) {
        return new DomainEntity(domain.getRootDomain());
    }

    public Domain toDomain() {
        return new Domain(domainId, rootDomain);
    }
}
