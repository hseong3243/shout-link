package com.seong.shoutlink.domain.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TrieTest {

    private final String[] words = {
        "apple", "ant", "arm", "anchor", "angle",
        "alphabet", "album", "alien", "apron", "atom",
        "axe", "art", "architect", "area", "arena",
        "altitude", "almond", "aroma", "arrow", "ash",
        "auction", "audio", "attic", "author", "award",
        "charge", "crown", "cross", "cup", "cap",
        "crop", "card", "chip", "case", "camp",
        "club", "court", "crash", "class", "corner"
    };

    @Nested
    @DisplayName("insert 호출 시")
    class InsertTest {

        @Test
        @DisplayName("성공: 단어가 삽입된다.")
        void insert() {
            //given
            Trie trie = new Trie();
            String word = "hello";

            //when
            trie.insert(word);

            //then
            List<String> result = trie.search("hello", 20);
            assertThat(result).hasSize(1);
        }

        @ParameterizedTest
        @CsvSource({
            "35, 1",
            "36, 0"
        })
        @DisplayName("성공: 삽입할 수 있는 단어의 최대 길이는 35자이다.")
        void insertableWordMaxLength_35(int wordLength, int resultSize) {
            //given
            Trie trie = new Trie();
            String word = "a".repeat(wordLength);

            //when
            trie.insert(word);

            //then
            List<String> result = trie.search(word, 20);
            assertThat(result).hasSize(resultSize);
        }

        @RepeatedTest(20)
        @DisplayName("성공: 동시에 삽입되어도 손실되지 않는다.")
        void concurrentInsert() throws InterruptedException {
            //given
            Trie trie = new Trie();
            String prefix = "";
            int threadPoolSize = words.length;
            ExecutorService service = Executors.newFixedThreadPool(threadPoolSize);
            CountDownLatch latch = new CountDownLatch(threadPoolSize);

            //when
            for (int i = 0; i < threadPoolSize; i++) {
                int finalI = i;
                service.submit(() -> {
                    try {
                        trie.insert(words[finalI]);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            //then
            List<String> result = trie.search(prefix, 100);
            assertThat(result).hasSize(threadPoolSize);
        }
    }

    @Nested
    @DisplayName("search 호출 시")
    class SearchTest {

        @Test
        @DisplayName("성공: 검색어 목록을 반환한다.")
        void returnWordList() {
            //given
            Trie trie = new Trie();
            String wordA = "hello";
            String wordB = "hell";
            String wordC = "helllllllssssso";
            trie.insert(wordA);
            trie.insert(wordB);
            trie.insert(wordC);

            //when
            List<String> result = trie.search("he", 30);

            //then
            assertThat(result).containsExactlyInAnyOrder(wordA, wordB, wordC);
        }

        @Test
        @DisplayName("성공: 검색어 목록의 최대 길이는 100개이다.")
        void maxWordListSize_100() {
            //given
            Trie trie = new Trie();
            String prefix = "asdf";
            StringBuilder sb = new StringBuilder(prefix);
            for (int i = 0; i < 26; i++) {
                sb.append((char) ('a' + i));
                for (int j = 0; j < 5; j++) {
                    sb.append((char) ('a' + j));
                    trie.insert(sb.toString());
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.deleteCharAt(sb.length() - 1);
            }

            //when
            List<String> result = trie.search(prefix, 130);

            //then
            assertThat(result).hasSize(100);
        }

        @Test
        @DisplayName("성공: 유효한 검색어의 길이는 최대 30자이다.")
        void maxSearchPrefix_30() {
            //given
            Trie trie = new Trie();
            String prefix = "a".repeat(30);
            trie.insert(prefix + "a");
            trie.insert(prefix + "b");
            trie.insert(prefix + "c");
            trie.insert(prefix + "d");
            trie.insert(prefix + "e");
            prefix += "abcde";

            //when
            List<String> result = trie.search(prefix, 20);

            //then
            assertThat(result).hasSize(5);
        }
    }
}
