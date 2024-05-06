package com.seong.shoutlink.domain.link.repository;

import com.seong.shoutlink.domain.member.repository.MemberEntity;
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
@DiscriminatorValue("member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_link_bundle")
public class MemberLinkBundleEntity extends LinkBundleEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    public MemberLinkBundleEntity(String description, boolean isDefault, MemberEntity memberEntity) {
        super(description, isDefault);
        this.member = memberEntity;
    }
}
