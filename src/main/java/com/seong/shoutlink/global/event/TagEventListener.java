package com.seong.shoutlink.global.event;

import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.link.service.event.CreateHubLinkEvent;
import com.seong.shoutlink.domain.tag.service.TagService;
import com.seong.shoutlink.domain.tag.service.request.AutoCreateHubTagCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
public class TagEventListener {

    private final TagService tagService;

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createHubTags(CreateHubLinkEvent event) {
        AutoCreateHubTagCommand command = new AutoCreateHubTagCommand(event.hubId());
        try {
            tagService.autoCreateHubTags(command);
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
