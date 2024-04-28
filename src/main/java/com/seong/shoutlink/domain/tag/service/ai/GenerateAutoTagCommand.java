package com.seong.shoutlink.domain.tag.service.ai;

import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.LinkBundleAndLinks;
import com.seong.shoutlink.domain.link.LinkBundle;
import java.util.List;

public record GenerateAutoTagCommand(List<AutoTagLinkBundle> linkBundles, int generateTagCount) {

    public record AutoTagLinkBundle(String description, List<AutoTagLink> links) {

        public static AutoTagLinkBundle from(LinkBundle linkBundle, List<AutoTagLink> autoTagLinks) {
            return new AutoTagLinkBundle(linkBundle.getDescription(), autoTagLinks);
        }
    }

    public record AutoTagLink(String url, String description) {

        public static AutoTagLink from(Link link) {
            return new AutoTagLink(link.getUrl(), link.getDescription());
        }
    }

    public static GenerateAutoTagCommand create(
        List<LinkBundleAndLinks> linkBundlesAndLinks,
        int generateTagCount) {
        List<AutoTagLinkBundle> content = linkBundlesAndLinks.stream()
            .map(linkBundleAndLinks -> {
                List<AutoTagLink> autoTagLinks = linkBundleAndLinks.getLinks().stream()
                    .map(AutoTagLink::from)
                    .toList();
                return AutoTagLinkBundle.from(linkBundleAndLinks.getLinkBundle(), autoTagLinks);
            })
            .toList();
        return new GenerateAutoTagCommand(content, generateTagCount);
    }
}
