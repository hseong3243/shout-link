package com.seong.shoutlink.domain.hub.repository;

import com.seong.shoutlink.domain.common.BaseEntity;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.member.repository.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hub_member")
public class HubMemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hubMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id", nullable = false, updatable = false)
    private HubEntity hub;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HubMemberRole hubMemberRole;

    public HubMemberEntity(MemberEntity memberEntity, HubEntity hubEntity, HubMemberRole hubMemberRole) {
        this.member = memberEntity;
        this.hub = hubEntity;
        this.hubMemberRole = hubMemberRole;
    }

    public static HubMemberEntity create(MemberEntity memberEntity, HubEntity hubEntity) {
        return new HubMemberEntity(memberEntity, hubEntity, HubMemberRole.MASTER);
    }

    public Hub toHub() {
        return new Hub(
            hub.getHubId(),
            member.getMemberId(),
            hub.getName(),
            hub.getDescription(),
            hub.isPrivate());
    }
}
