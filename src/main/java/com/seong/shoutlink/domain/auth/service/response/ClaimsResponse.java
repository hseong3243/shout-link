package com.seong.shoutlink.domain.auth.service.response;

import com.seong.shoutlink.domain.member.MemberRole;
import java.util.List;

public record ClaimsResponse(Long memberId, MemberRole memberRole, List<String> authorities) {

}
