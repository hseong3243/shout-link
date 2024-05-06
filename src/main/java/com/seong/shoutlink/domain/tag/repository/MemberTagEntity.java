package com.seong.shoutlink.domain.tag.repository;

import com.seong.shoutlink.domain.member.repository.MemberEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTagEntity extends TagEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    public MemberTagEntity(String name, MemberEntity memberEntity) {
        super(name);
        this.member = memberEntity;
    }
}
