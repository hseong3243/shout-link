package com.seong.shoutlink.global.client.ai;

import java.util.List;

public record GeminiRequest(List<Content> contents) {

    record Content(List<Part> parts) {

    }

    record Part(String text) {

    }

    static GeminiRequest create(String text) {
        Part part = new Part(text);
        Content content = new Content(List.of(part));
        return new GeminiRequest(List.of(content));
    }
}
