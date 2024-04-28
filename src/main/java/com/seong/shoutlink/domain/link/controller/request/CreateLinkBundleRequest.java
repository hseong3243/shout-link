package com.seong.shoutlink.domain.link.controller.request;

import java.util.Objects;

public record CreateLinkBundleRequest(
    String description,
    Boolean isDefault) {

    public CreateLinkBundleRequest(String description, Boolean isDefault) {
        this.description = description;
        this.isDefault = Objects.isNull(isDefault) ? false : isDefault;
    }
}
