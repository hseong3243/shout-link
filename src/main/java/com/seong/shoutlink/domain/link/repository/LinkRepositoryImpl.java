package com.seong.shoutlink.domain.link.repository;

import com.seong.shoutlink.domain.link.LinkWithLinkBundle;
import com.seong.shoutlink.domain.link.service.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LinkRepositoryImpl implements LinkRepository {

    private final LinkJpaRepository linkJpaRepository;

    @Override
    public Long save(LinkWithLinkBundle linkWithLinkBundle) {
        LinkEntity linkEntity = linkJpaRepository.save(LinkEntity.create(linkWithLinkBundle));
        return linkEntity.getLinkId();
    }
}
