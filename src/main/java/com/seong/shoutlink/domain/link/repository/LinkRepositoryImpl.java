package com.seong.shoutlink.domain.link.repository;

import com.seong.shoutlink.domain.link.LinkWithLinkBundle;
import com.seong.shoutlink.domain.link.service.LinkRepository;
import com.seong.shoutlink.domain.link.service.result.LinkPaginationResult;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public LinkPaginationResult findLinks(Member member, LinkBundle linkBundle, int page,
        int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return null;
    }
}
