package com.seong.shoutlink.domain.linkbundle.repository;

import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.service.LinkBundleRepository;
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
        return linkBundleEntity.getLinkBundleId();
    }
}
