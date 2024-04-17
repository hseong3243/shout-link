package com.seong.shoutlink.global.event;

import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.link.service.event.CreateHubLinkEvent;
import com.seong.shoutlink.domain.link.service.event.CreateMemberLinkEvent;
import com.seong.shoutlink.domain.tag.service.TagUseCase;
import com.seong.shoutlink.domain.tag.service.request.AutoCreateHubTagCommand;
import com.seong.shoutlink.domain.tag.service.request.AutoCreateMemberTagCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
public class TagEventListener {

    private final TagUseCase tagUseCase;

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createHubTags(CreateHubLinkEvent event) {
        AutoCreateHubTagCommand command = new AutoCreateHubTagCommand(event.hubId());
        try {
            tagUseCase.autoCreateHubTags(command);
            log.debug("[Tag] 링크 개수가 최소 태그 자동 생성 조건을 만족");
        } catch (ShoutLinkException e) {
            if(e.getErrorCode().equals(ErrorCode.NOT_MET_CONDITION)) {
                log.debug("[Tag] 링크 개수가 최소 태그 자동 생성 조건을 만족하지 않음");
            } else {
                throw e;
            }
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createMemberTags(CreateMemberLinkEvent event) {
        AutoCreateMemberTagCommand command = new AutoCreateMemberTagCommand(event.memberId());
        try {
            tagUseCase.autoCreateMemberTags(command);
            log.debug("[Tag] 링크 개수가 최소 태그 자동 생성 조건을 만족");
        } catch (ShoutLinkException e) {
            if(e.getErrorCode().equals(ErrorCode.NOT_MET_CONDITION)) {
                log.debug("[Tag] 링크 개수가 최소 태그 자동 생성 조건을 만족하지 않음");
            } else {
                throw e;
            }
        }
    }
}
