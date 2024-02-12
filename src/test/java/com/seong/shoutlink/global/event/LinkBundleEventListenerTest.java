package com.seong.shoutlink.global.event;

import static org.assertj.core.api.Assertions.assertThat;

import com.seong.shoutlink.domain.auth.service.AuthService;
import com.seong.shoutlink.domain.auth.service.request.CreateMemberCommand;
import com.seong.shoutlink.domain.linkbundle.repository.LinkBundleEntity;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.fixture.MemberFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.RecordApplicationEvents;

@SpringBootTest
@RecordApplicationEvents
class LinkBundleEventListenerTest {

    @Autowired
    private AuthService authService;

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
            authService.createMember(createMemberCommand);

            //then
            LinkBundleEntity linkBundleEntity = em.createQuery(
                    "select mlb from MemberLinkBundleEntity mlb where mlb.memberId = :memberId",
                    LinkBundleEntity.class)
                .setParameter("memberId", member.getMemberId())
                .getSingleResult();
            assertThat(linkBundleEntity.getDescription()).isEqualTo("기본");
        }
    }
}
