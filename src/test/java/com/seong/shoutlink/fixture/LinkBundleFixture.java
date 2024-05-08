package com.seong.shoutlink.fixture;

import com.seong.shoutlink.domain.link.linkbundle.LinkBundle;

public final class LinkBundleFixture {

    public static final long LINK_BUNDLE_ID = 1L;
    public static final String DESCRIPTION = "기본 분류";
    public static final boolean IS_DEFAULT = true;

    public static LinkBundle linkBundle() {
        return new LinkBundle(LINK_BUNDLE_ID, DESCRIPTION, IS_DEFAULT);
    }
}
