package com.seong.shoutlink.domain.member.repository;

import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class StubMemberRepository implements MemberRepository {

    private Map<Long, Member> memory = new HashMap<>();

    public StubMemberRepository(Member... members) {
        stub(members);
    }

    public void stub(Member... members) {
        for (Member member : members) {
            memory.put(getNextId(), member);
        }
    }

    public Long getNextId() {
        return (long) (memory.size() + 1);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memory.values().stream().filter(member -> member.getEmail().equals(email)).findFirst();
    }

    @Override
    public Optional<Member> findByNickname(String nickname) {
        return memory.values().stream().filter(member -> member.getNickname().equals(nickname)).findFirst();
    }

    @Override
    public Long save(Member member) {
        return (long) (memory.size() + 1);
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return Optional.ofNullable(memory.get(memberId));
    }
}
