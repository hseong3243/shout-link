package com.seong.shoutlink.domain.hub;

import com.seong.shoutlink.domain.exception.ErrorCode;
import com.seong.shoutlink.domain.exception.ShoutLinkException;
import com.seong.shoutlink.domain.member.Member;
import java.text.MessageFormat;
import java.util.Objects;
import lombok.Getter;

@Getter
public class Hub {

    private static final int NAME_MAX_SIZE = 30;
    private static final String DESCRIPTION_DEFAULT = "";
    private static final int DESCRIPTION_MAX_SIZE = 200;

    private final Long hubId;
    private final Long masterId;
    private final String name;
    private final String description;
    private final boolean isPrivate;

    public Hub(Member master, String name, String description, boolean isPrivate) {
        this(null, master.getMemberId(), name, description, isPrivate);
    }

    public Hub(Long hubId, Long memberId, String name, String description, boolean isPrivate) {
        this.hubId = hubId;
        this.masterId = memberId;
        this.name = validateName(name);
        this.description = validateDescription(description);
        this.isPrivate = isPrivate;
    }

    private String validateName(String name) {
        if (Objects.isNull(name)) {
            throw new ShoutLinkException("허브 이름은 필수입니다.", ErrorCode.ILLEGAL_ARGUMENT);
        }
        if(name.isEmpty() || name.length() > NAME_MAX_SIZE) {
            throw new ShoutLinkException(
                MessageFormat.format("허브 이름은 1자 이상, {0}자 이하여야 합니다.", NAME_MAX_SIZE),
                ErrorCode.ILLEGAL_ARGUMENT);
        }
        return name;
    }

    private String validateDescription(String description) {
        if(Objects.isNull(description)) {
            return DESCRIPTION_DEFAULT;
        }
        if(description.length() > DESCRIPTION_MAX_SIZE) {
            throw new ShoutLinkException(
                MessageFormat.format("허브 설명은 {0}자 이하여야 합니다.", DESCRIPTION_MAX_SIZE),
                ErrorCode.ILLEGAL_ARGUMENT);
        }
        return description;
    }

    public void checkMasterAuthority(Member member) {
        if(Objects.equals(masterId, member.getMemberId())) {
            return;
        }
        throw new ShoutLinkException("권한이 없습니다.", ErrorCode.UNAUTHORIZED);
    }
}
