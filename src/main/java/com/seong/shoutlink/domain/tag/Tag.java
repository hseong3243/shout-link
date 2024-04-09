package com.seong.shoutlink.domain.tag;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Tag {

    private Long tagId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Tag(String name) {
        this(null, name, null, null);
    }

    public Tag(Long tagId, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.tagId = tagId;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public boolean isCreatedWithinADay() {
        LocalDateTime aDayAgo = LocalDateTime.now().minusDays(1);
        return createdAt.isAfter(aDayAgo);
    }
}
