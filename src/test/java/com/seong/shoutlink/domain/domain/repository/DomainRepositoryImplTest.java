package com.seong.shoutlink.domain.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.seong.shoutlink.base.BaseIntegrationTest;
import com.seong.shoutlink.domain.domain.Domain;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DomainRepositoryImplTest extends BaseIntegrationTest {

    @Autowired
    private DomainRepositoryImpl domainRepository;

    @Nested
    @DisplayName("save 호출 시")
    class SaveTest {

        @Test
        @DisplayName("성공: 루트 도메인 문자열이 캐싱된다.")
        void cachingRootDomain() {
            //given
            Domain domain = new Domain("asdf");

            //when
            domainRepository.save(domain);

            //then
            List<String> rootDomains = domainRepository.findRootDomains("as", 10);
            assertThat(rootDomains).containsExactly("asdf");
        }
    }
}
