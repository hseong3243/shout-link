package com.seong.shoutlink.domain.link.linkbundle.repository;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.repository.HubEntity;
import com.seong.shoutlink.domain.hub.repository.HubJpaRepository;
import com.seong.shoutlink.domain.link.linkbundle.HubLinkBundle;
import com.seong.shoutlink.domain.link.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.link.linkbundle.MemberLinkBundle;
import com.seong.shoutlink.domain.link.linkbundle.service.LinkBundleRepository;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.repository.MemberEntity;
import com.seong.shoutlink.domain.member.repository.MemberJpaRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LinkBundleRepositoryImpl implements LinkBundleRepository {

    private final LinkBundleJpaRepository linkBundleJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final HubJpaRepository hubJpaRepository;

    @Override
    public Long save(MemberLinkBundle memberLinkBundle) {
        Member member = memberLinkBundle.getMember();
        LinkBundle linkBundle = memberLinkBundle.getLinkBundle();
        MemberEntity memberEntity = memberJpaRepository.findById(member.getMemberId())
            .orElseThrow(NoSuchElementException::new);
        LinkBundleEntity linkBundleEntity = LinkBundleEntity.create(linkBundle, memberEntity);
        linkBundleJpaRepository.save(linkBundleEntity);
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
        LinkBundle linkBundle = hubLinkBundle.getLinkBundle();
        Hub hub = hubLinkBundle.getHub();
        HubEntity hubEntity = hubJpaRepository.findById(hub.getHubId())
            .orElseThrow(NoSuchElementException::new);
        LinkBundleEntity linkBundleEntity = LinkBundleEntity.create(linkBundle, hubEntity);
        linkBundleJpaRepository.save(linkBundleEntity);
        return linkBundleEntity.getLinkBundleId();
    }

    @Override
    public Optional<LinkBundle> findHubLinkBundle(Long linkBundleId, Hub hub) {
        return linkBundleJpaRepository.findHubLinkBundle(linkBundleId, hub.getHubId())
            .map(LinkBundleEntity::toDomain);
    }

    @Override
    public List<LinkBundle> findHubLinkBundles(Hub hub) {
        return linkBundleJpaRepository.findAllByHubId(hub.getHubId())
            .stream()
            .map(LinkBundleEntity::toDomain)
            .toList();
    }

    @Override
    public Optional<LinkBundle> findMemberLinkBundle(Long linkBundleId, Member member) {
        return linkBundleJpaRepository.findMemberLinkBundle(linkBundleId, member.getMemberId())
            .map(LinkBundleEntity::toDomain);
    }
}
