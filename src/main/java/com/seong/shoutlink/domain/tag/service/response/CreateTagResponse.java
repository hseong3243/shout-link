package com.seong.shoutlink.domain.tag.service.response;

import com.seong.shoutlink.domain.tag.Tag;
import java.util.List;

public record CreateTagResponse(List<Long> tagIds) {

    public static CreateTagResponse from(List<Tag> tags) {
        List<Long> tagIds = tags.stream()
            .map(Tag::getTagId)
            .toList();
        return new CreateTagResponse(tagIds);
    }
}
