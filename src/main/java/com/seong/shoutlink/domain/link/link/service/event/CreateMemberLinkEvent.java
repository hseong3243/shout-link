package com.seong.shoutlink.domain.link.link.service.event;

import java.util.Objects;

public class CreateMemberLinkEvent extends CreateLinkEvent{

    private final Long memberId;

    public CreateMemberLinkEvent(Long linkId, String url, Long memberId) {
        super(linkId, url);
        this.memberId = memberId;
    }

    public Long memberId() {
        return memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        CreateMemberLinkEvent that = (CreateMemberLinkEvent) o;
        return Objects.equals(memberId, that.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), memberId);
    }

    @Override
    public String toString() {
        return "CreateMemberLinkEvent{" +
            "memberId=" + memberId +
            '}';
    }
}
