package com.seong.shoutlink.domain.member.service;

import com.seong.shoutlink.domain.member.Member;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Long save(Member member);
}
