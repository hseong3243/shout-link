package com.seong.shoutlink.domain.linkbundle.repository;

import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.service.LinkBundleRepository;
import com.seong.shoutlink.domain.member.Member;
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
}
