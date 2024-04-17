package com.seong.shoutlink.domain.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.domain.domain.Domain;
import com.seong.shoutlink.domain.domain.repository.StubDomainRepository;
import com.seong.shoutlink.domain.domain.service.request.FindDomainCommand;
import com.seong.shoutlink.domain.domain.service.request.FindDomainLinksCommand;
import com.seong.shoutlink.domain.domain.service.request.FindDomainsCommand;
import com.seong.shoutlink.domain.domain.service.request.FindRootDomainsCommand;
import com.seong.shoutlink.domain.domain.service.request.UpdateDomainCommand;
import com.seong.shoutlink.domain.domain.service.response.FindDomainDetailResponse;
import com.seong.shoutlink.domain.domain.service.response.FindDomainLinksResponse;
import com.seong.shoutlink.domain.domain.service.response.FindDomainsResponse;
import com.seong.shoutlink.domain.domain.service.response.FindRootDomainsResponse;
import com.seong.shoutlink.domain.domain.service.response.UpdateDomainResponse;
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

class DomainServiceTest {

    StubDomainRepository domainRepository;
    StubLinkRepository linkRepository;
    DomainService domainService;

    @Nested
    @DisplayName("updateDomain 메서드 호출 시")
    class UpdateDomainTest {

        @BeforeEach
        void setUp() {
            domainRepository = new StubDomainRepository();
            linkRepository = new StubLinkRepository();
            domainService = new DomainService(domainRepository, linkRepository);
        }

        @Test
        @DisplayName("성공: 도메인 정보가 없으면 새롭게 생성됨")
        void createNewDomain() {
            //given
            Link link = LinkFixture.link();
            linkRepository.stub(link);
            UpdateDomainCommand command = new UpdateDomainCommand(link.getLinkId(), link.getUrl());

            //when
            UpdateDomainResponse response = domainService.updateDomain(command);

            //then
            assertThat(response.domainId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("성공: 도메인 정보가 있으면 기존 정보를 이용함")
        void updateDomain() {
            //given
            Link link = LinkFixture.link();
            Domain domain = DomainFixture.domain();
            linkRepository.stub(link);
            domainRepository.stub(domain);
            UpdateDomainCommand command = new UpdateDomainCommand(link.getLinkId(), link.getUrl());

            //when
            UpdateDomainResponse response = domainService.updateDomain(command);

            //then
            assertThat(response.domainId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("예외(notFound): 존재하지 않는 링크")
        void notFound_WhenLinkNotFound() {
            //given
            UpdateDomainCommand command = new UpdateDomainCommand(1L, "github.com");

            //when
            Exception exception = catchException(() -> domainService.updateDomain(command));

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
            domainRepository = new StubDomainRepository();
            linkRepository = new StubLinkRepository();
            domainService = new DomainService(domainRepository, linkRepository);
        }

        @Test
        @DisplayName("성공: 루트 도메인 문자열 목록을 반환한다.")
        void findRootDomains() {
            //given
            String keyword = "git";
            FindRootDomainsCommand command = new FindRootDomainsCommand(keyword, 10);
            String rootDomain = "github.com";
            Domain domain = DomainFixture.domain(rootDomain);
            domainRepository.stub(domain);

            //when
            FindRootDomainsResponse response = domainService.findRootDomains(command);

            //then
            assertThat(response.rootDomains()).containsExactly(rootDomain);
        }
    }

    @Nested
    @DisplayName("findDomains 호출 시")
    class FindDomainsTest {

        @BeforeEach
        void setUp() {
            domainRepository = new StubDomainRepository();
            linkRepository = new StubLinkRepository();
            domainService = new DomainService(domainRepository, linkRepository);
        }

        @Test
        @DisplayName("성공: 도메인 목록을 반환한다.")
        void findDomains() {
            //given
            String keyword = "keyword";
            int page = 0;
            int size = 10;
            FindDomainsCommand command = new FindDomainsCommand(keyword, page, size);
            Domain domain = DomainFixture.domain();
            domainRepository.stub(domain);

            //when
            FindDomainsResponse response = domainService.findDomains(command);

            //then
            assertThat(response.domains()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("findDomain 호출 시")
    class FindDomainTest {

        @BeforeEach
        void setUp() {
            domainRepository = new StubDomainRepository();
            linkRepository = new StubLinkRepository();
            domainService = new DomainService(domainRepository, linkRepository);
        }

        @Test
        @DisplayName("성공: 도메인을 조회한다.")
        void findDomain() {
            //given
            Domain domain = DomainFixture.domain();
            FindDomainCommand command = new FindDomainCommand(1L);
            domainRepository.stub(domain);

            //when
            FindDomainDetailResponse response = domainService.findDomain(command);

            //then
            assertThat(response.domainId()).isEqualTo(domain.getDomainId());
            assertThat(response.rootDomain()).isEqualTo(domain.getRootDomain());
        }

        @Test
        @DisplayName("예외(notFound): 존재하지 않는 도메인")
        void notFound_whenDomainNotFound() {
            //given
            FindDomainCommand command = new FindDomainCommand(1L);

            //when
            Exception exception = catchException(() -> domainService.findDomain(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("findDomainLinks 호출 시")
    class FindDomainLinksTest {

        @BeforeEach
        void setUp() {
            domainRepository = new StubDomainRepository();
            linkRepository = new StubLinkRepository();
            domainService = new DomainService(domainRepository, linkRepository);
        }

        @Test
        @DisplayName("성공: 도메인 링크 목록을 조회한다.")
        void findDomainLinks() {
            //given
            FindDomainLinksCommand command = new FindDomainLinksCommand(1L, 0, 10);
            Link link = LinkFixture.link();
            Domain domain = DomainFixture.domain();
            domainRepository.stub(domain, link);

            //when
            FindDomainLinksResponse response = domainService.findDomainLinks(command);

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
            FindDomainLinksCommand command = new FindDomainLinksCommand(1L, 0, 10);

            //when
            Exception exception = catchException(() -> domainService.findDomainLinks(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }
    }
}