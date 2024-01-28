package com.seong.shoutlink.domain.member.repository;

import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class StubMemberRepository implements MemberRepository {

    private List<Member> memory = new ArrayList<>();

    public StubMemberRepository(Member... members) {
        memory.addAll(Arrays.stream(members).toList());
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memory.stream().filter(member -> member.getEmail().equals(email)).findFirst();
    }

    @Override
    public Optional<Member> findByNickname(String nickname) {
        return memory.stream().filter(member -> member.getNickname().equals(nickname)).findFirst();
    }

    @Override
    public Long save(Member member) {
        return (long) (memory.size() + 1);
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memory.stream().filter(member -> member.getMemberId().equals(memberId)).findFirst();
    }
}
