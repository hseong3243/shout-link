package com.seong.shoutlink.domain.linkbundle;

import java.util.Objects;
import lombok.Getter;

@Getter
public class LinkBundle {

    private Long linkBundleId;
    private String description;
    private boolean isDefault;

    public LinkBundle(String description, boolean isDefault) {
        this(null, description, isDefault);
    }

    public LinkBundle(Long linkBundleId, String description, boolean isDefault) {
        this.linkBundleId = linkBundleId;
        this.description = description;
        this.isDefault = isDefault;
    }

    public void initId(Long linkBundleId) {
        if(Objects.isNull(this.linkBundleId)) {
            this.linkBundleId = linkBundleId;
        }
    }
}
