package com.seong.shoutlink.global.event;

import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.link.link.service.event.CreateHubLinkEvent;
import com.seong.shoutlink.domain.link.link.service.event.CreateMemberLinkEvent;
import com.seong.shoutlink.domain.tag.service.TagUseCase;
import com.seong.shoutlink.domain.tag.service.request.AutoCreateHubTagCommand;
import com.seong.shoutlink.domain.tag.service.request.AutoCreateMemberTagCommand;
import com.seong.shoutlink.domain.tag.service.response.CreateTagResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
public class TagEventListener {

    private final TagUseCase tagUseCase;

    @Async(value = "generativeTaskExecutor")
    @TransactionalEventListener
    public void createHubTags(CreateHubLinkEvent event) {
        AutoCreateHubTagCommand command = new AutoCreateHubTagCommand(event.hubId());
        try {
            CreateTagResponse response = tagUseCase.autoCreateHubTags(command);
            log.debug("[Event] 허브 태그 자동 생성. tagIds={}", response.tagIds());
        } catch (ShoutLinkException e) {
            if(e.getErrorCode().equals(ErrorCode.NOT_MET_CONDITION)) {
                log.debug("[Event] 회원 태그 자동 생성 취소. 조건을 만족하지 않음.");
            } else {
                throw e;
            }
        }
    }

    @Async(value = "generativeTaskExecutor")
    @TransactionalEventListener
    public void createMemberTags(CreateMemberLinkEvent event) {
        AutoCreateMemberTagCommand command = new AutoCreateMemberTagCommand(event.memberId());
        try {
            CreateTagResponse response = tagUseCase.autoCreateMemberTags(command);
            log.debug("[Event] 회원 태그 자동 생성. tagIds={}", response.tagIds());
        } catch (ShoutLinkException e) {
            if(e.getErrorCode().equals(ErrorCode.NOT_MET_CONDITION)) {
                log.debug("[Event] 회원 태그 자동 생성 취소. 조건을 만족하지 않음.");
            } else {
                throw e;
            }
        }
    }
}
