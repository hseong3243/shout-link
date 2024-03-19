package com.seong.shoutlink.domain.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.domain.domain.Domain;
import com.seong.shoutlink.domain.domain.repository.StubDomainRepository;
import com.seong.shoutlink.domain.domain.service.request.UpdateDomainCommand;
import com.seong.shoutlink.domain.domain.service.response.UpdateDomainResponse;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.repository.FakeLinkRepository;
import com.seong.shoutlink.fixture.DomainFixture.DomainFixture;
import com.seong.shoutlink.fixture.LinkFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DomainServiceTest {

    StubDomainRepository domainRepository;
    FakeLinkRepository linkRepository;
    DomainService domainService;

    @Nested
    @DisplayName("updateDomain 메서드 호출 시")
    class UpdateDomainTest {

        @BeforeEach
        void setUp() {
            domainRepository = new StubDomainRepository();
            linkRepository = new FakeLinkRepository();
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
}