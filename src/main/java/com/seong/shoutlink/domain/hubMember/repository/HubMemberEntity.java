package com.seong.shoutlink.domain.hubmember.repository;

import com.seong.shoutlink.domain.common.BaseEntity;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.repository.HubEntity;
import com.seong.shoutlink.domain.hubmember.HubMemberRole;
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

    @Column(nullable = false)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id", nullable = false, updatable = false)
    private HubEntity hubEntity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HubMemberRole hubMemberRole;

    public HubMemberEntity(Long memberId, HubEntity hubEntity, HubMemberRole hubMemberRole) {
        this.memberId = memberId;
        this.hubEntity = hubEntity;
        this.hubMemberRole = hubMemberRole;
    }

    public static HubMemberEntity create(HubEntity hubEntity, Long masterId) {
        return new HubMemberEntity(masterId, hubEntity, HubMemberRole.MASTER);
    }

    public Hub toHub() {
        return new Hub(
            hubEntity.getHubId(),
            memberId,
            hubEntity.getName(),
            hubEntity.getDescription(),
            hubEntity.isPrivate());
    }
}
