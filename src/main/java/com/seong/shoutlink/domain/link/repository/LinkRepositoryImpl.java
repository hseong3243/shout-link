package com.seong.shoutlink.domain.link.repository;

import com.seong.shoutlink.domain.domain.Domain;
import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.LinkWithLinkBundle;
import com.seong.shoutlink.domain.link.service.LinkRepository;
import com.seong.shoutlink.domain.link.service.result.LinkPaginationResult;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public LinkPaginationResult findLinks(
        LinkBundle linkBundle,
        int page,
        int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<LinkEntity> linkEntityPage = linkJpaRepository.findAllByLinkBundleId(
            linkBundle.getLinkBundleId(),
            pageRequest);
        List<Link> content = linkEntityPage.map(LinkEntity::toDomain).getContent();
        return new LinkPaginationResult(
            content,
            linkEntityPage.getTotalElements(),
            linkEntityPage.hasNext());
    }

    @Override
    public void updateLinkDomain(Link link, Domain domain) {
        linkJpaRepository.findById(link.getLinkId())
            .ifPresent(linkEntity -> linkEntity.updateDomainId(domain));
    }

    @Override
    public Optional<Link> findById(Long linkId) {
        return linkJpaRepository.findById(linkId)
            .map(LinkEntity::toDomain);
    }
}
