package com.seong.shoutlink.domain.link.linkbundle.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.base.BaseRepositoryTest;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.link.linkbundle.HubLinkBundle;
import com.seong.shoutlink.domain.link.linkbundle.LinkBundle;
import com.seong.shoutlink.domain.link.linkbundle.MemberLinkBundle;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.fixture.HubFixture;
import com.seong.shoutlink.fixture.LinkBundleFixture;
import com.seong.shoutlink.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LinkBundleRepositoryImplTest extends BaseRepositoryTest {

    @Autowired
    LinkBundleRepositoryImpl linkBundleRepository;

    @Nested
    @DisplayName("save 호출 시")
    class SaveTest {

        @Nested
        @DisplayName("MemberLinkBundle을 인수로 전달했을 때")
        class Arg_MemberLinkBundle {

            @Test
            @DisplayName("예외(illegalStateEx): 존재하지 않는 회원 조회 시")
            void illegalStateEx_WhenTryToFindMember_DoseNotExist() {
                //given
                Member member = MemberFixture.member();
                LinkBundle linkBundle = LinkBundleFixture.linkBundle();
                MemberLinkBundle memberLinkBundle = new MemberLinkBundle(member, linkBundle);

                //when
                Exception exception = catchException(
                    () -> linkBundleRepository.save(memberLinkBundle));

                //then
                assertThat(exception).isInstanceOf(IllegalStateException.class);
            }
        }

        @Nested
        @DisplayName("HubLinkBundle을 인수로 전달했을 때")
        class Arg_HubLinkBundle {

            @Test
            @DisplayName("예외(illegalStateEx): 존재하지 않는 허브 조회 시")
            void illegalStateEx_WhenTryToFindHub_DoseNotExist() {
                //given
                Member member = MemberFixture.member();
                Hub hub = HubFixture.hub(member);
                LinkBundle linkBundle = LinkBundleFixture.linkBundle();
                HubLinkBundle hubLinkBundle = new HubLinkBundle(hub, linkBundle);

                //when
                Exception exception = catchException(
                    () -> linkBundleRepository.save(hubLinkBundle));

                //then
                assertThat(exception).isInstanceOf(IllegalStateException.class);
            }
        }
    }
}