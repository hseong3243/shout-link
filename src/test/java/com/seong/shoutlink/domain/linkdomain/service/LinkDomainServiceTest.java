package com.seong.shoutlink.domain.linkdomain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.domain.link.linkdomain.LinkDomain;
import com.seong.shoutlink.domain.link.linkdomain.service.LinkDomainService;
import com.seong.shoutlink.domain.linkdomain.repository.StubLinkDomainRepository;
import com.seong.shoutlink.domain.link.linkdomain.service.request.FindLinkDomainCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.request.FindLinkDomainLinksCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.request.FindLinkDomainsCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.request.FindRootDomainsCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.request.UpdateLinkDomainCommand;
import com.seong.shoutlink.domain.link.linkdomain.service.response.FindLinkDomainDetailResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.response.FindLinkDomainLinksResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.response.FindLinkDomainsResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.response.FindRootDomainsResponse;
import com.seong.shoutlink.domain.link.linkdomain.service.response.UpdateLinkDomainResponse;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.repository.StubLinkRepository;
import com.seong.shoutlink.fixture.DomainFixture.DomainFixture;
import com.seong.shoutlink.fixture.LinkFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LinkDomainServiceTest {

    StubLinkDomainRepository linkDomainRepository;
    StubLinkRepository linkRepository;
    LinkDomainService linkDomainService;

    @Nested
    @DisplayName("updateLinkDomain 메서드 호출 시")
    class UpdateLinkDomainTest {

        @BeforeEach
        void setUp() {
            linkDomainRepository = new StubLinkDomainRepository();
            linkRepository = new StubLinkRepository();
            linkDomainService = new LinkDomainService(linkDomainRepository, linkRepository);
        }

        @Test
        @DisplayName("성공: 도메인 정보가 없으면 새롭게 생성됨")
        void createNewLinkDomain() {
            //given
            Link link = LinkFixture.link();
            linkRepository.stub(link);
            UpdateLinkDomainCommand command = new UpdateLinkDomainCommand(link.getLinkId(), link.getUrl());

            //when
            UpdateLinkDomainResponse response = linkDomainService.updateLinkDomain(command);

            //then
            assertThat(response.domainId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("성공: 도메인 정보가 있으면 기존 정보를 이용함")
        void updateLinkDomain() {
            //given
            Link link = LinkFixture.link();
            LinkDomain linkDomain = DomainFixture.domain();
            linkRepository.stub(link);
            linkDomainRepository.stub(linkDomain);
            UpdateLinkDomainCommand command = new UpdateLinkDomainCommand(link.getLinkId(), link.getUrl());

            //when
            UpdateLinkDomainResponse response = linkDomainService.updateLinkDomain(command);

            //then
            assertThat(response.domainId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("예외(notFound): 존재하지 않는 링크")
        void notFound_WhenLinkNotFound() {
            //given
            UpdateLinkDomainCommand command = new UpdateLinkDomainCommand(1L, "github.com");

            //when
            Exception exception = catchException(() -> linkDomainService.updateLinkDomain(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("findRootDomains 호출 시")
    class FindRootDomainsTest {

        @BeforeEach
        void setUp() {
            linkDomainRepository = new StubLinkDomainRepository();
            linkRepository = new StubLinkRepository();
            linkDomainService = new LinkDomainService(linkDomainRepository, linkRepository);
        }

        @Test
        @DisplayName("성공: 루트 도메인 문자열 목록을 반환한다.")
        void findRootDomains() {
            //given
            String keyword = "git";
            FindRootDomainsCommand command = new FindRootDomainsCommand(keyword, 10);
            String rootDomain = "github.com";
            LinkDomain linkDomain = DomainFixture.domain(rootDomain);
            linkDomainRepository.stub(linkDomain);

            //when
            FindRootDomainsResponse response = linkDomainService.findRootDomains(command);

            //then
            assertThat(response.rootDomains()).containsExactly(rootDomain);
        }
    }

    @Nested
    @DisplayName("findLinkDomains 호출 시")
    class FindLinkDomainsTest {

        @BeforeEach
        void setUp() {
            linkDomainRepository = new StubLinkDomainRepository();
            linkRepository = new StubLinkRepository();
            linkDomainService = new LinkDomainService(linkDomainRepository, linkRepository);
        }

        @Test
        @DisplayName("성공: 도메인 목록을 반환한다.")
        void findLinkDomains() {
            //given
            String keyword = "keyword";
            int page = 0;
            int size = 10;
            FindLinkDomainsCommand command = new FindLinkDomainsCommand(keyword, page, size);
            LinkDomain linkDomain = DomainFixture.domain();
            linkDomainRepository.stub(linkDomain);

            //when
            FindLinkDomainsResponse response = linkDomainService.findLinkDomains(command);

            //then
            assertThat(response.domains()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("findLinkDomain 호출 시")
    class FindLinkDomainTest {

        @BeforeEach
        void setUp() {
            linkDomainRepository = new StubLinkDomainRepository();
            linkRepository = new StubLinkRepository();
            linkDomainService = new LinkDomainService(linkDomainRepository, linkRepository);
        }

        @Test
        @DisplayName("성공: 도메인을 조회한다.")
        void findLinkDomain() {
            //given
            LinkDomain linkDomain = DomainFixture.domain();
            FindLinkDomainCommand command = new FindLinkDomainCommand(1L);
            linkDomainRepository.stub(linkDomain);

            //when
            FindLinkDomainDetailResponse response = linkDomainService.findLinkDomain(command);

            //then
            assertThat(response.domainId()).isEqualTo(linkDomain.getDomainId());
            assertThat(response.rootDomain()).isEqualTo(linkDomain.getRootDomain());
        }

        @Test
        @DisplayName("예외(notFound): 존재하지 않는 도메인")
        void notFound_whenDomainNotFound() {
            //given
            FindLinkDomainCommand command = new FindLinkDomainCommand(1L);

            //when
            Exception exception = catchException(() -> linkDomainService.findLinkDomain(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("findLinkDomainLinks 호출 시")
    class FindLinkDomainLinksTest {

        @BeforeEach
        void setUp() {
            linkDomainRepository = new StubLinkDomainRepository();
            linkRepository = new StubLinkRepository();
            linkDomainService = new LinkDomainService(linkDomainRepository, linkRepository);
        }

        @Test
        @DisplayName("성공: 도메인 링크 목록을 조회한다.")
        void findLinkDomainLinks() {
            //given
            FindLinkDomainLinksCommand command = new FindLinkDomainLinksCommand(1L, 0, 10);
            Link link = LinkFixture.link();
            LinkDomain linkDomain = DomainFixture.domain();
            linkDomainRepository.stub(linkDomain, link);

            //when
            FindLinkDomainLinksResponse response = linkDomainService.findLinkDomainLinks(command);

            //then
            assertThat(response.links()).hasSize(1)
                .allSatisfy(findLink -> {
                    assertThat(findLink.linkId()).isEqualTo(link.getLinkId());
                    assertThat(findLink.url()).isEqualTo(link.getUrl());
                    assertThat(findLink.aggregationCount()).isEqualTo(1);
                });
        }

        @Test
        @DisplayName("예외(notFound): 존재하지 않는 도메인")
        void notFound_WhenDomainNotFound() {
            //given
            FindLinkDomainLinksCommand command = new FindLinkDomainLinksCommand(1L, 0, 10);

            //when
            Exception exception = catchException(() -> linkDomainService.findLinkDomainLinks(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }
    }
}