package com.seong.shoutlink.domain.link.repository;

import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.service.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LinkRepositoryImpl implements LinkRepository {

    private final LinkJpaRepository linkJpaRepository;

    @Override
    public Long save(Link link) {
        LinkEntity linkEntity = linkJpaRepository.save(LinkEntity.create(link));
        return linkEntity.getLinkId();
    }
}
