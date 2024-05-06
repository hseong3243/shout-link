package com.seong.shoutlink.domain.tag;

import com.seong.shoutlink.domain.member.Member;
import lombok.Getter;

@Getter
public class MemberTag {

    private final Member member;
    private final Tag tag;

    public MemberTag(Member member, Tag tag) {
        this.member = member;
        this.tag = tag;
    }

    public Long getMemberId() {
        return member.getMemberId();
    }
}
