package com.seong.shoutlink.domain.linkbundle;

import lombok.Getter;

@Getter
public class LinkBundle {

    private final Long linkBundleId;
    private final String description;
    private final boolean isDefault;

    public LinkBundle(String description, boolean isDefault) {
        this(null, description, isDefault);
    }

    public LinkBundle(Long linkBundleId, String description, boolean isDefault) {
        this.linkBundleId = linkBundleId;
        this.description = description;
        this.isDefault = isDefault;
    }
}
