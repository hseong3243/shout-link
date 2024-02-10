package com.seong.shoutlink.domain.linkbundle.repository;

import com.seong.shoutlink.domain.linkbundle.LinkBundle;
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
    public Long save(LinkBundle linkBundle) {
        LinkBundleEntity linkBundleEntity = LinkBundleEntity.create(linkBundle);
        linkBundleJpaRepository.save(linkBundleEntity);
        linkBundle.initId(linkBundleEntity.getLinkBundleId());
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
}