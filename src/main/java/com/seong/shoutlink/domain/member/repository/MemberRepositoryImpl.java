package com.seong.shoutlink.domain.member.repository;

import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.service.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
            .map(MemberEntity::toDomain);
    }

    @Override
    public Optional<Member> findByNickname(String nickname) {
        return memberJpaRepository.findByNickname(nickname)
            .map(MemberEntity::toDomain);
    }

    @Override
    public Long save(Member member) {
        MemberEntity memberEntity = memberJpaRepository.save(MemberEntity.create(member));
        return memberEntity.getMemberId();
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberJpaRepository.findById(memberId)
            .map(MemberEntity::toDomain);
    }
}
