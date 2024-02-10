package com.seong.shoutlink.domain.hubMember.repository;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hubMember.HubMemberRole;
import com.seong.shoutlink.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hubMemberId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long hubId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HubMemberRole hubMemberRole;

    public HubMemberEntity(Long memberId, Long hubId, HubMemberRole hubMemberRole) {
        this.memberId = memberId;
        this.hubId = hubId;
        this.hubMemberRole = hubMemberRole;
    }

    public static HubMemberEntity create(Hub hub, Member member) {
        return new HubMemberEntity(member.getMemberId(), hub.getHubId(), HubMemberRole.MASTER);
    }
}
