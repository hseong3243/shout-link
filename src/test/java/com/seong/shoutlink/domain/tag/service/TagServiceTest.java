package com.seong.shoutlink.domain.tag.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.hub.repository.StubHubRepository;
import com.seong.shoutlink.domain.link.Link;
import com.seong.shoutlink.domain.link.repository.FakeLinkRepository;
import com.seong.shoutlink.domain.linkbundle.repository.FakeLinkBundleRepository;
import com.seong.shoutlink.domain.member.repository.StubMemberRepository;
import com.seong.shoutlink.domain.tag.Tag;
import com.seong.shoutlink.domain.tag.repository.StubTagRepository;
import com.seong.shoutlink.domain.tag.service.request.AutoCreateMemberTagCommand;
import com.seong.shoutlink.domain.tag.service.request.AutoCreateHubTagCommand;
import com.seong.shoutlink.domain.tag.service.response.CreateTagResponse;
import com.seong.shoutlink.fixture.ApiFixture;
import com.seong.shoutlink.fixture.HubFixture;
import com.seong.shoutlink.fixture.LinkBundleFixture;
import com.seong.shoutlink.fixture.LinkFixture;
import com.seong.shoutlink.fixture.MemberFixture;
import com.seong.shoutlink.fixture.TagFixture;
import com.seong.shoutlink.global.client.StubApiClient;
import com.seong.shoutlink.global.client.ai.GeminiClient;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TagServiceTest {

    TagService tagService;
    StubTagRepository tagRepository;
    StubHubRepository hubRepository;
    StubMemberRepository memberRepository;
    FakeLinkBundleRepository linkBundleRepository;
    FakeLinkRepository linkRepository;
    AutoGenerativeClient autoGenerativeClient;

    @BeforeEach
    void setUp() {
        tagRepository = new StubTagRepository();
        hubRepository = new StubHubRepository();
        memberRepository = new StubMemberRepository();
        linkBundleRepository = new FakeLinkBundleRepository();
        linkRepository = new FakeLinkRepository();
        ObjectMapper objectMapper = new ObjectMapper().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        StubApiClient apiClient = new StubApiClient();
        autoGenerativeClient = new GeminiClient("fakeUrl", "fakeApiKey", objectMapper, apiClient);

        apiClient.stub(ApiFixture.geminiResponse());
    }

    @Nested
    @DisplayName("autoCreateHubTag 호출 시")
    class AutoCreateTagTest {

        @BeforeEach
        void setUp() {
            tagService = new TagService(tagRepository, memberRepository,
                hubRepository, linkBundleRepository, linkRepository, autoGenerativeClient);
        }

        @ParameterizedTest
        @CsvSource({"5", "10", "15", "20", "25"})
        @DisplayName("성공: 링크가 5의 배수인 경우 자동 태그 생성한다.")
        void autoCreateTag(int totalLinkSize) {
            //given
            AutoCreateHubTagCommand command = new AutoCreateHubTagCommand(1L);

            hubRepository.stub(HubFixture.hub(MemberFixture.member()));
            linkBundleRepository.stub(LinkBundleFixture.linkBundle());
            linkRepository.stub(LinkFixture.links(totalLinkSize).toArray(Link[]::new));

            //when
            CreateTagResponse response = tagService.autoCreateHubTags(command);

            //then
            assertThat(response.tagIds()).hasSize(ApiFixture.DEFAULT_FIXED_TAG_COUNT);
        }

        @Test
        @DisplayName("성공: 기존 태그는 삭제한다.")
        void thenDeleteOldTags() {
            //given
            AutoCreateHubTagCommand command = new AutoCreateHubTagCommand(1L);

            hubRepository.stub(HubFixture.hub(MemberFixture.member()));
            linkBundleRepository.stub(LinkBundleFixture.linkBundle());
            linkRepository.stub(LinkFixture.links(5).toArray(Link[]::new));
            tagRepository.stub(TagFixture.tag());

            //when
            tagService.autoCreateHubTags(command);

            //then
            assertThat(tagRepository.count()).isEqualTo(ApiFixture.DEFAULT_FIXED_TAG_COUNT);
        }

        @Test
        @DisplayName("예외(notFound): 존재하지 않는 허브인 경우")
        void notFound_WhenHubNotFound() {
            //given
            AutoCreateHubTagCommand command = new AutoCreateHubTagCommand(1L);

            //when
            Exception exception = catchException(() -> tagService.autoCreateHubTags(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }

        @Test
        @DisplayName("예외(notMetCondition): 하루 안에 생성된 태그가 존재하는 경우")
        void notMetCondition_WhenLatestTagCreatedWithinADay() {
            //given
            AutoCreateHubTagCommand command = new AutoCreateHubTagCommand(1L);
            LocalDateTime createdWithinADay = LocalDateTime.now().minusHours(23);
            Tag tag = new Tag(1L, "태그", createdWithinADay, createdWithinADay);

            hubRepository.stub(HubFixture.hub(MemberFixture.member()));
            tagRepository.stub(tag);

            //when
            Exception exception = catchException(() -> tagService.autoCreateHubTags(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_MET_CONDITION);
        }

        @ParameterizedTest
        @CsvSource({"0", "1", "2", "3", "4"})
        @DisplayName("예외(notMetCondition): 링크가 5개 미만인 경우")
        void notMetCondition_WhenTotalLinkCountLT5(int totalLinkSize) {
            //given
            AutoCreateHubTagCommand command = new AutoCreateHubTagCommand(1L);

            hubRepository.stub(HubFixture.hub(MemberFixture.member()));
            linkBundleRepository.stub(LinkBundleFixture.linkBundle());
            linkRepository.stub(LinkFixture.links(totalLinkSize).toArray(Link[]::new));

            //when
            Exception exception = catchException(() -> tagService.autoCreateHubTags(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_MET_CONDITION);
        }

        @ParameterizedTest
        @CsvSource({"6", "9", "11", "14", "16", "19", "21", "24"})
        @DisplayName("예외(notMetCondition): 링크 개수가 5의 배수가 아닐 때")
        void notMetCondition_WhenTotalLinkCountIsNotMultiplesOf5(int totalLinkSize) {
            //given
            AutoCreateHubTagCommand command = new AutoCreateHubTagCommand(1L);

            hubRepository.stub(HubFixture.hub(MemberFixture.member()));
            linkBundleRepository.stub(LinkBundleFixture.linkBundle());
            linkRepository.stub(LinkFixture.links(totalLinkSize).toArray(Link[]::new));

            //when
            Exception exception = catchException(() -> tagService.autoCreateHubTags(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_MET_CONDITION);
        }
    }

    @Nested
    @DisplayName("autoCreateMemberTags 호출 시")
    class AutoCreateMemberTagsTest {

        @BeforeEach
        void setUp() {
            tagService = new TagService(tagRepository, memberRepository,
                hubRepository, linkBundleRepository, linkRepository, autoGenerativeClient);
        }

        @ParameterizedTest
        @CsvSource({"5", "10", "15", "20", "25"})
        @DisplayName("성공: 링크가 5의 배수인 경우 자동 태그 생성한다.")
        void autoCreateMemberTags(int totalLinkSize) {
            //given
            AutoCreateMemberTagCommand command = new AutoCreateMemberTagCommand(1L);

            memberRepository.stub(MemberFixture.member());
            linkBundleRepository.stub(LinkBundleFixture.linkBundle());
            linkRepository.stub(LinkFixture.links(totalLinkSize).toArray(Link[]::new));

            //when
            CreateTagResponse response = tagService.autoCreateMemberTags(command);

            //then
            assertThat(response.tagIds()).hasSize(ApiFixture.DEFAULT_FIXED_TAG_COUNT);
        }

        @Test
        @DisplayName("성공: 기존 태그는 삭제한다.")
        void thenDeleteOldTags() {
            //given
            AutoCreateMemberTagCommand command = new AutoCreateMemberTagCommand(1L);

            memberRepository.stub(MemberFixture.member());
            linkBundleRepository.stub(LinkBundleFixture.linkBundle());
            linkRepository.stub(LinkFixture.links(5).toArray(Link[]::new));
            tagRepository.stub(TagFixture.tag());

            //when
            tagService.autoCreateMemberTags(command);

            //then
            assertThat(tagRepository.count()).isEqualTo(ApiFixture.DEFAULT_FIXED_TAG_COUNT);
        }

        @Test
        @DisplayName("예외(notFound): 존재하지 않는 회원인 경우")
        void notFound_WhenMemberNotFound() {
            //given
            AutoCreateMemberTagCommand command = new AutoCreateMemberTagCommand(1L);

            //when
            Exception exception = catchException(() -> tagService.autoCreateMemberTags(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
        }

        @Test
        @DisplayName("예외(notMetCondition): 하루 안에 생성된 태그가 존재하는 경우")
        void notMetCondition_WhenLatestTagCreatedWithinADay() {
            //given
            AutoCreateMemberTagCommand command = new AutoCreateMemberTagCommand(1L);
            LocalDateTime createdWithinADay = LocalDateTime.now().minusHours(23);
            Tag tag = new Tag(1L, "태그", createdWithinADay, createdWithinADay);

            memberRepository.stub(MemberFixture.member());
            tagRepository.stub(tag);

            //when
            Exception exception = catchException(() -> tagService.autoCreateMemberTags(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_MET_CONDITION);
        }

        @ParameterizedTest
        @CsvSource({"0", "1", "2", "3", "4"})
        @DisplayName("예외(notMetCondition): 링크가 5개 미만인 경우")
        void notMetCondition_WhenTotalLinkCountLT5(int totalLinkSize) {
            //given
            AutoCreateMemberTagCommand command = new AutoCreateMemberTagCommand(1L);

            memberRepository.stub(MemberFixture.member());
            linkBundleRepository.stub(LinkBundleFixture.linkBundle());
            linkRepository.stub(LinkFixture.links(totalLinkSize).toArray(Link[]::new));

            //when
            Exception exception = catchException(() -> tagService.autoCreateMemberTags(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_MET_CONDITION);
        }

        @ParameterizedTest
        @CsvSource({"6", "9", "11", "14", "16", "19", "21", "24"})
        @DisplayName("예외(notMetCondition): 링크 개수가 5의 배수가 아닐 때")
        void notMetCondition_WhenTotalLinkCountIsNotMultiplesOf5(int totalLinkSize) {
            //given
            AutoCreateMemberTagCommand command = new AutoCreateMemberTagCommand(1L);

            memberRepository.stub(MemberFixture.member());
            linkBundleRepository.stub(LinkBundleFixture.linkBundle());
            linkRepository.stub(LinkFixture.links(totalLinkSize).toArray(Link[]::new));

            //when
            Exception exception = catchException(() -> tagService.autoCreateMemberTags(command));

            //then
            assertThat(exception).isInstanceOf(ShoutLinkException.class)
                .extracting(e -> ((ShoutLinkException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_MET_CONDITION);
        }
    }
}
