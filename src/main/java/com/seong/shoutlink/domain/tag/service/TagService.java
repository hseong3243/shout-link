package com.seong.shoutlink.domain.tag.service;

import static java.util.stream.Collectors.*;

import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.hub.service.HubRepository;
import com.seong.shoutlink.domain.link.LinkWithLinkBundle;
import com.seong.shoutlink.domain.link.service.LinkRepository;
import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.linkbundle.service.LinkBundleRepository;
import com.seong.shoutlink.domain.link.LinkBundleAndLinks;
import com.seong.shoutlink.domain.tag.HubTag;
import com.seong.shoutlink.domain.tag.Tag;
import com.seong.shoutlink.domain.tag.service.ai.GenerateAutoTagCommand;
import com.seong.shoutlink.domain.tag.service.request.AutoCreateTagCommand;
import com.seong.shoutlink.domain.tag.service.response.CreateTagResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagService {

    private static final int MINIMUM_TAG_CONDITION = 5;
    private static final int MAXIMUM_TAG_COUNT = 5;

    private final TagRepository tagRepository;
    private final HubRepository hubRepository;
    private final LinkBundleRepository linkBundleRepository;
    private final LinkRepository linkRepository;
    private final AutoGenerativeClient autoGenerativeClient;

    @Transactional
    public CreateTagResponse autoCreateHubTags(AutoCreateTagCommand command) {
        Hub hub = getHub(command.hubId());
        List<LinkBundle> hubLinkBundles = linkBundleRepository.findHubLinkBundles(hub);
        List<LinkWithLinkBundle> links = linkRepository.findAllByLinkBundlesIn(
            hubLinkBundles);

        List<LinkBundleAndLinks> linkBundlesAndLinks = groupingLinks(links);
        int generateTagCount = calculateNumberOfTag(links);
        GenerateAutoTagCommand generateAutoTagCommand = GenerateAutoTagCommand
            .create(linkBundlesAndLinks, generateTagCount);

        List<HubTag> tags = autoGenerativeClient.generateTags(generateAutoTagCommand)
            .stream()
            .map(generatedTag -> new Tag(generatedTag.name()))
            .map(tag -> new HubTag(hub, tag))
            .toList();
        return CreateTagResponse.from(tagRepository.saveAll(tags));
    }

    private List<LinkBundleAndLinks> groupingLinks(List<LinkWithLinkBundle> links) {
        return links.stream()
            .collect(groupingBy(
                LinkWithLinkBundle::getLinkBundle,
                mapping(LinkWithLinkBundle::getLink, toList())))
            .entrySet().stream()
            .map(entry -> new LinkBundleAndLinks(entry.getKey(), entry.getValue()))
            .toList();
    }

    private int calculateNumberOfTag(List<LinkWithLinkBundle> links) {
        int totalLinkCount = links.size();
        if (totalLinkCount < MINIMUM_TAG_CONDITION) {
            throw new ShoutLinkException("태그 생성 조건을 충족하지 못했습니다.", ErrorCode.NOT_MET_CONDITION);
        }
        return Math.min(MAXIMUM_TAG_COUNT, totalLinkCount / MINIMUM_TAG_CONDITION);
    }

    private Hub getHub(Long hubId) {
        return hubRepository.findById(hubId)
            .orElseThrow(() -> new ShoutLinkException("존재하지 않는 허브입니다.", ErrorCode.NOT_FOUND));
    }
}
