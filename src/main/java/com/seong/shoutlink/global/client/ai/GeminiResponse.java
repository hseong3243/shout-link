package com.seong.shoutlink.global.client.ai;

import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import java.util.List;

public record GeminiResponse(List<Candidate> candidates) {

    record Candidate(Content content) {

        public String getResponse() {
            return content.getResponse();
        }
    }

    record Content(List<Part> parts, String role) {

        public String getResponse() {
            Part part = parts.stream()
                .findFirst()
                .orElseThrow(() -> new ShoutLinkException("", ErrorCode.ILLEGAL_ARGUMENT));
            return part.text();
        }
    }

    record Part(String text) {

    }
}
