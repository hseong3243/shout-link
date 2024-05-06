package com.seong.shoutlink.global.event;

import static org.assertj.core.api.Assertions.assertThat;

import com.seong.shoutlink.base.BaseIntegrationTest;
import com.seong.shoutlink.domain.hub.service.HubService;
import com.seong.shoutlink.domain.hub.service.request.CreateHubCommand;
import com.seong.shoutlink.domain.hub.service.response.CreateHubResponse;
import com.seong.shoutlink.domain.link.repository.LinkBundleEntity;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import com.seong.shoutlink.domain.member.service.MemberService;
import com.seong.shoutlink.domain.member.service.request.CreateMemberCommand;
import com.seong.shoutlink.fixture.MemberFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.RecordApplicationEvents;

@RecordApplicationEvents
class LinkBundleEventListenerTest extends BaseIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private HubService hubService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    @Nested
    @DisplayName("createMemberEvent 수신 시")
    class CreateMemberEventTest {

        @Test
        @DisplayName("성공: 기본 링크 번들 생성됨")
        void createDefaultLinkBundle() {
            //given
            Member member = MemberFixture.member();
            CreateMemberCommand createMemberCommand = new CreateMemberCommand(
                member.getEmail(),
                member.getPassword(),
                member.getNickname());

            //when
            memberService.createMember(createMemberCommand);

            //then
            LinkBundleEntity linkBundleEntity = em.createQuery(
                    "select mlb from MemberLinkBundleEntity mlb where mlb.member.memberId = :memberId",
                    LinkBundleEntity.class)
                .setParameter("memberId", member.getMemberId())
                .getSingleResult();
            assertThat(linkBundleEntity.getDescription()).isEqualTo("기본");
        }
    }

    @Nested
    @DisplayName("createHubEvent 수신 시")
    class CreateHubEventTest {

        @Test
        @DisplayName("성공: 기본 허브 링크 번들 생성됨")
        void createDefaultHubLinkBundle() {
            //given
            Member member = MemberFixture.member();
            Long memberId = memberRepository.save(member);
            CreateHubCommand command = new CreateHubCommand(memberId, "허브", "설명", false);

            //when
            CreateHubResponse response = hubService.createHub(command);

            //then
            LinkBundleEntity linkBundleEntity = em.createQuery(
                    "select hlb from HubLinkBundleEntity hlb "
                        + "where hlb.hub.hubId = :hubId", LinkBundleEntity.class)
                .setParameter("hubId", response.hubId())
                .getSingleResult();
            assertThat(linkBundleEntity.getDescription()).isEqualTo("기본");
        }
    }
}
