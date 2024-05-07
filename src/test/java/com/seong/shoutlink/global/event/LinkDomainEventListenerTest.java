package com.seong.shoutlink.global.event;

import static org.assertj.core.api.Assertions.assertThat;

import com.seong.shoutlink.base.BaseIntegrationTest;
import com.seong.shoutlink.domain.linkdomain.LinkDomain;
import com.seong.shoutlink.domain.linkdomain.repository.LinkDomainEntity;
import com.seong.shoutlink.domain.linkdomain.service.LinkDomainRepository;
import com.seong.shoutlink.domain.link.repository.LinkEntity;
import com.seong.shoutlink.domain.link.service.LinkService;
import com.seong.shoutlink.domain.link.service.request.CreateLinkCommand;
import com.seong.shoutlink.domain.link.service.response.CreateLinkResponse;
import com.seong.shoutlink.domain.link.LinkBundle;
import com.seong.shoutlink.domain.link.MemberLinkBundle;
import com.seong.shoutlink.domain.link.repository.LinkBundleEntity;
import com.seong.shoutlink.domain.link.service.LinkBundleRepository;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import com.seong.shoutlink.fixture.DomainFixture.DomainFixture;
import com.seong.shoutlink.fixture.LinkBundleFixture;
import com.seong.shoutlink.fixture.MemberFixture;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.RecordApplicationEvents;

@RecordApplicationEvents
class LinkDomainEventListenerTest extends BaseIntegrationTest {

    @Autowired
    private LinkService linkService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LinkBundleRepository linkBundleRepository;

    @Autowired
    private LinkDomainRepository linkDomainRepository;

    @Autowired
    private EntityManager em;

    @Nested
    @DisplayName("createLinkEvent 수신 시")
    class CreateLinkEventTest {

        @Test
        @DisplayName("성공: 도메인 정보가 없으면 생성 됨")
        void createDomainInfo() {
            //given
            Member member = MemberFixture.member();
            memberRepository.save(member);
            LinkBundle linkBundle = LinkBundleFixture.linkBundle();
            MemberLinkBundle memberLinkBundle = new MemberLinkBundle(member, linkBundle);
            linkBundleRepository.save(memberLinkBundle);
            CreateLinkCommand command = new CreateLinkCommand(member.getMemberId(),
                linkBundle.getLinkBundleId(),
                "github.com/hseong3243", "내 깃허브");

            List<LinkBundleEntity> a = em.createQuery(
                    "select lb from LinkBundleEntity lb", LinkBundleEntity.class)
                .getResultList();
            System.out.println(a.size());

            //when
            linkService.createLink(command);

            //then
            List<LinkDomainEntity> result = em.createQuery("select d from LinkDomainEntity d",
                    LinkDomainEntity.class)
                .getResultList();
            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("성공: 도메인 정보가 있으면 생성되지 않음")
        void updateDomainInfo() {
            //given
            Member member = MemberFixture.member();
            memberRepository.save(member);
            LinkBundle linkBundle = LinkBundleFixture.linkBundle();
            MemberLinkBundle memberLinkBundle = new MemberLinkBundle(member, linkBundle);
            linkBundleRepository.save(memberLinkBundle);
            LinkDomain linkDomain = DomainFixture.domain();
            linkDomainRepository.save(linkDomain);
            CreateLinkCommand command = new CreateLinkCommand(member.getMemberId(),
                linkBundle.getLinkBundleId(),
                linkDomain.getRootDomain(), "내 깃허브");

            //when
            linkService.createLink(command);

            //then
            List<LinkDomainEntity> result = em.createQuery("select d from LinkDomainEntity d",
                    LinkDomainEntity.class)
                .getResultList();
            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("성공: 링크 도메인 식별자가 업데이트 됨")
        void updateLinkDomainId() {
            //given
            Member member = MemberFixture.member();
            memberRepository.save(member);
            LinkBundle linkBundle = LinkBundleFixture.linkBundle();
            MemberLinkBundle memberLinkBundle = new MemberLinkBundle(member, linkBundle);
            linkBundleRepository.save(memberLinkBundle);
            LinkDomain linkDomain = DomainFixture.domain();
            CreateLinkCommand command = new CreateLinkCommand(member.getMemberId(),
                linkBundle.getLinkBundleId(),
                "github.com/hseong3243", "내 깃허브");

            //when
            CreateLinkResponse response = linkService.createLink(command);

            //then
            LinkEntity linkEntity = em.find(LinkEntity.class, response.linkId());
            assertThat(linkEntity.getDomainId()).isNotNull();
        }
    }
}