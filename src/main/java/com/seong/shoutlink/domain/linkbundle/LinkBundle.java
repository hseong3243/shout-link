package com.seong.shoutlink.domain.linkbundle;

import com.seong.shoutlink.domain.member.Member;
import lombok.Getter;

@Getter
public class LinkBundle {

    private Long linkBundleId;
    private String description;
    private boolean isDefault;
    private Member member;

    public LinkBundle(String description, boolean isDefault, Member member) {
        this.description = description;
        this.isDefault = isDefault;
        this.member = member;
    }
}
