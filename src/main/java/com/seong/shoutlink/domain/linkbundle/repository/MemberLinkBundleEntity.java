package com.seong.shoutlink.domain.linkbundle.repository;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue("member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLinkBundleEntity extends LinkBundleEntity {

    private Long memberId;

    public MemberLinkBundleEntity(String description, boolean isDefault, Long memberId) {
        super(description, isDefault);
        this.memberId = memberId;
    }
}
