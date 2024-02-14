package com.seong.shoutlink.domain.member.repository;

import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.domain.member.MemberRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String email;
    private String password;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    private MemberEntity(String email, String password, String nickname, MemberRole memberRole) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.memberRole = memberRole;
    }

    public static MemberEntity create(final Member member) {
        return new MemberEntity(
            member.getEmail(),
            member.getPassword(),
            member.getNickname(),
            member.getMemberRole()
        );
    }

    public Member toDomain() {
        return new Member(
            memberId,
            email,
            password,
            nickname,
            memberRole
        );
    }
}
