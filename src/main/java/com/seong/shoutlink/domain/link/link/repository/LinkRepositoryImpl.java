package com.seong.shoutlink.domain.link.link.repository;

import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.link.link.Link;
import com.seong.shoutlink.domain.link.link.LinkBundleAndLink;
import com.seong.shoutlink.domain.link.link.service.LinkRepository;
import com.seong.shoutlink.domain.link.link.service.result.LinkPaginationResult;
import com.seong.shoutlink.domain.link.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.link.linkbundle.repository.LinkBundleEntity;
import com.seong.shoutlink.domain.link.linkbundle.repository.LinkBundleJpaRepository;
import com.seong.shoutlink.domain.link.linkdomain.LinkDomain;
import com.seong.shoutlink.domain.link.linkdomain.repository.LinkDomainCacheRepository;
import com.seong.shoutlink.domain.link.linkdomain.repository.LinkDomainEntity;
import com.seong.shoutlink.domain.link.linkdomain.repository.LinkDomainJpaRepository;
import com.seong.shoutlink.domain.link.linkdomain.util.DomainExtractor;
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

    private final LinkBundleJpaRepository linkBundleJpaRepository;
    private final LinkJpaRepository linkJpaRepository;
    private final LinkDomainJpaRepository linkDomainJpaRepository;
    private final LinkDomainCacheRepository linkDomainCacheRepository;

    @Override
    public Long save(LinkBundleAndLink linkBundleAndLink) {
        Link link = linkBundleAndLink.getLink();
        LinkBundle linkBundle = linkBundleAndLink.getLinkBundle();
        String rootDomain = DomainExtractor.extractRootDomain(link.getUrl());
        LinkDomainEntity linkDomainEntity = linkDomainJpaRepository.findByRootDomain(rootDomain)
            .orElseGet(() -> {
                linkDomainCacheRepository.insert(rootDomain);
                return linkDomainJpaRepository.save(new LinkDomainEntity(rootDomain));
            });
        LinkBundleEntity linkBundleEntity = linkBundleJpaRepository.findById(
                linkBundle.getLinkBundleId())
            .orElseThrow(IllegalStateException::new);

        return linkJpaRepository.save(LinkEntity.create(link, linkBundleEntity, linkDomainEntity))
            .getLinkId();
    }

    @Override
    public LinkPaginationResult findLinks(
        LinkBundle linkBundle,
        int page,
        int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<LinkEntity> linkEntityPage = linkJpaRepository.findAllByLinkBundleLinkBundleId(
            linkBundle.getLinkBundleId(),
            pageRequest);
        List<Link> content = linkEntityPage.map(LinkEntity::toDomain).getContent();
        return new LinkPaginationResult(
            content,
            linkEntityPage.getTotalElements(),
            linkEntityPage.hasNext());
    }

    @Override
    public void updateLinkDomain(Link link, LinkDomain linkDomain) {
//        linkJpaRepository.findById(link.getLinkId())
//            .ifPresent(linkEntity -> linkEntity.updateDomainId(linkDomain));
    }

    @Override
    public Optional<Link> findById(Long linkId) {
        return linkJpaRepository.findById(linkId)
            .map(LinkEntity::toDomain);
    }

    @Override
    public List<LinkBundleAndLink> findAllByLinkBundlesIn(List<LinkBundle> linkBundles) {
        List<Long> linkBundleIds = linkBundles.stream()
            .map(LinkBundle::getLinkBundleId)
            .toList();
        List<LinkEntity> linkEntities = linkJpaRepository.findAllByLinkBundleLinkBundleIdIn(linkBundleIds);
        Map<Long, LinkBundle> linkBundleIdAndLinkBundle = linkBundles.stream()
            .collect(Collectors.toMap(LinkBundle::getLinkBundleId, linkBundle -> linkBundle));
        return linkEntities.stream()
            .map(linkEntity -> new LinkBundleAndLink(linkEntity.toDomain(),
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
