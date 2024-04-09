package com.seong.shoutlink.domain.tag;

import lombok.Getter;

@Getter
public class Tag {

    private Long tagId;
    private String name;

    public Tag(String name) {
        this(null, name);
    }

    public Tag(Long tagId, String name) {
        this.tagId = tagId;
        this.name = name;
    }
}
