package com.seong.shoutlink.domain.linkbundle.repository;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.linkbundle.HubLinkBundle;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.MemberLinkBundle;
import com.seong.shoutlink.domain.linkbundle.service.LinkBundleRepository;
import com.seong.shoutlink.domain.member.Member;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LinkBundleRepositoryImpl implements LinkBundleRepository {

    private final LinkBundleJpaRepository linkBundleJpaRepository;

    @Override
    public Long save(MemberLinkBundle memberLinkBundle) {
        LinkBundleEntity linkBundleEntity = LinkBundleEntity.create(memberLinkBundle);
        linkBundleJpaRepository.save(linkBundleEntity);
        memberLinkBundle.initLinkBundleId(linkBundleEntity.getLinkBundleId());
        return linkBundleEntity.getLinkBundleId();
    }

    @Override
    public void updateDefaultBundleFalse(Member member) {
        linkBundleJpaRepository.updateDefaultBundleFalse(member.getMemberId());
    }

    @Override
    public Optional<LinkBundle> findById(Long linkBundleId) {
        return linkBundleJpaRepository.findById(linkBundleId)
            .map(LinkBundleEntity::toDomain);
    }

    @Override
    public List<LinkBundle> findLinkBundlesThatMembersHave(Member member) {
        return linkBundleJpaRepository.findAllByMemberId(member.getMemberId()).stream()
            .map(LinkBundleEntity::toDomain)
            .toList();
    }

    @Override
    public Long save(HubLinkBundle hubLinkBundle) {
        LinkBundleEntity linkBundleEntity = LinkBundleEntity.create(hubLinkBundle);
        linkBundleJpaRepository.save(linkBundleEntity);
        hubLinkBundle.initLinkBundleId(linkBundleEntity.getLinkBundleId());
        return linkBundleEntity.getLinkBundleId();
    }

    @Override
    public Optional<LinkBundle> findHubLinkBundle(Long linkBundleId, Hub hub) {
        return linkBundleJpaRepository.findHubLinkBundle(linkBundleId, hub.getHubId());
    }
}
