package com.seong.shoutlink.global.client.ai;

import com.seong.shoutlink.domain.tag.service.ai.GenerateAutoTagCommand;
import com.seong.shoutlink.domain.tag.service.ai.GenerateAutoTagCommand.AutoTagLink;
import com.seong.shoutlink.domain.tag.service.ai.GenerateAutoTagCommand.AutoTagLinkBundle;

public record AutoTagPrompt(String request, GenerateAutoTagCommand command) {

    public String toPromptString() {
        StringBuilder sb = new StringBuilder();
        sb.append("요청:").append(request).append("\n")
            .append("요약 대상:");
        sb.append("{");
        for (AutoTagLinkBundle linkBundle : command.linkBundles()) {
            sb.append("링크 묶음 설명:").append(linkBundle.description());
            sb.append("[");
            for (AutoTagLink link : linkBundle.links()) {
                sb.append("{");
                sb.append("링크 설명:").append(link.description()).append(",")
                    .append("url:").append(link.url());
                sb.append("}");
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }
}
