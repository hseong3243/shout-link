package com.seong.shoutlink.domain.hub.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.seong.shoutlink.base.BaseRepositoryTest;
import com.seong.shoutlink.domain.hub.Hub;
import com.seong.shoutlink.domain.member.Member;
import com.seong.shoutlink.fixture.HubFixture;
import com.seong.shoutlink.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class HubRepositoryImplTest extends BaseRepositoryTest {

    @Autowired
    HubRepositoryImpl hubRepository;

    @Nested
    @DisplayName("save 호출 시")
    class Save {

        @Test
        @DisplayName("예외(illegalStateEx): 존재하지 않는 회원 조회 시")
        void illegalStateEx_WhenTryToFindMember_DoseNotExist() {
            //given
            Member member = MemberFixture.member();
            Hub hub = HubFixture.hub(member);

            //when
            Exception exception = catchException(() -> hubRepository.save(hub));

            //then
            assertThat(exception).isInstanceOf(IllegalStateException.class);
        }
    }
}