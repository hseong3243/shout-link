package com.seong.shoutlink.domain.link.repository;

import com.seong.shoutlink.domain.domain.Domain;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.LinkWithLinkBundle;
import com.seong.shoutlink.domain.link.service.LinkRepository;
import com.seong.shoutlink.domain.link.service.result.LinkPaginationResult;
import com.seong.shoutlink.domain.link.LinkBundle;
import com.seong.shoutlink.domain.member.Member;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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

    @Override
    public List<LinkWithLinkBundle> findAllByLinkBundlesIn(List<LinkBundle> linkBundles) {
        List<Long> linkBundleIds = linkBundles.stream()
            .map(LinkBundle::getLinkBundleId)
            .toList();
        List<LinkEntity> linkEntities = linkJpaRepository.findAllByLinkBundleIdIn(linkBundleIds);
        Map<Long, LinkBundle> linkBundleIdAndLinkBundle = linkBundles.stream()
            .collect(Collectors.toMap(LinkBundle::getLinkBundleId, linkBundle -> linkBundle));
        return linkEntities.stream()
            .map(linkEntity -> new LinkWithLinkBundle(linkEntity.toDomain(),
                linkBundleIdAndLinkBundle.get(linkEntity.getLinkBundleId())))
            .toList();
    }

    @Override
    public Optional<Link> findMemberLink(Long linkId, Member member) {
        return linkJpaRepository.findByIdAndMemberId(linkId, member.getMemberId())
            .map(LinkEntity::toDomain);
    }

    @Override
    public Optional<Link> findHubLink(Long linkId, Hub hub) {
        return linkJpaRepository.findByIdAndHubId(linkId, hub.getHubId())
            .map(LinkEntity::toDomain);
    }

    @Override
    public void delete(Link link) {
        linkJpaRepository.deleteById(link.getLinkId());
    }
}
