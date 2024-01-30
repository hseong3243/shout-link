package com.seong.shoutlink.domain.linkbundle;

import java.util.Objects;
import lombok.Getter;

@Getter
public class LinkBundle {

    private Long linkBundleId;
    private String description;
    private boolean isDefault;
    private Long memberId;

    public LinkBundle(String description, boolean isDefault, Long memberId) {
        this.description = description;
        this.isDefault = isDefault;
        this.memberId = memberId;
    }

    public LinkBundle(Long linkBundleId, String description, boolean isDefault, Long memberId) {
        this.linkBundleId = linkBundleId;
        this.description = description;
        this.isDefault = isDefault;
        this.memberId = memberId;
    }

    public void initId(Long linkBundleId) {
        if(Objects.isNull(this.linkBundleId)) {
            this.linkBundleId = linkBundleId;
        }
    }
}
