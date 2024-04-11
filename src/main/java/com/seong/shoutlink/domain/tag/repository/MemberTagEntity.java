package com.seong.shoutlink.domain.tag.repository;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTagEntity extends TagEntity {

    private Long memberId;

    public MemberTagEntity(String name, Long memberId) {
        super(name);
        this.memberId = memberId;
    }
}
