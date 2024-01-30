package com.seong.shoutlink.domain.linkbundle.service;

import com.seong.shoutlink.domain.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.member.Member;

public interface LinkBundleRepository {

    Long save(LinkBundle linkBundle);

    void updateDefaultBundleFalse(Member member);
}
