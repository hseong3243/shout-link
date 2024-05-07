package com.seong.shoutlink.domain.linkdomain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.seong.shoutlink.base.BaseIntegrationTest;
import com.seong.shoutlink.domain.linkdomain.LinkDomain;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LinkDomainRepositoryImplTest extends BaseIntegrationTest {

    @Autowired
    private LinkDomainRepositoryImpl domainRepository;

    @Nested
    @DisplayName("save 호출 시")
    class SaveTest {

        @Test
        @DisplayName("성공: 루트 도메인 문자열이 캐싱된다.")
        void cachingRootDomain() {
            //given
            LinkDomain linkDomain = new LinkDomain("asdf");

            //when
            domainRepository.save(linkDomain);

            //then
            List<String> rootDomains = domainRepository.findRootDomains("as", 10);
            assertThat(rootDomains).containsExactly("asdf");
        }
    }
}
