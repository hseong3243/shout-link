package com.seong.shoutlink.domain.link;

import com.seong.shoutlink.domain.member.Member;
import lombok.Getter;

@Getter
public class MemberLinkBundle {

    private final Member member;
    private final LinkBundle linkBundle;

    public MemberLinkBundle(Member member, LinkBundle linkBundle) {
        this.member = member;
        this.linkBundle = linkBundle;
    }
}
