package com.seong.shoutlink.domain.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;

@Getter
public class Trie {

    static class Node {

        private final Map<Character, Node> children = new ConcurrentHashMap<>();
        private final AtomicBoolean isWord = new AtomicBoolean(false);

        public void settingWord() {
            isWord.compareAndSet(false, true);
        }

        public boolean hasChildren(char c) {
            return children.containsKey(c);
        }

        public Node nextNode(char c) {
            return children.computeIfAbsent(c, key -> new Node());
        }

        public void addSuggestions(String word, List<String> suggestions, int count) {
            if (isWord.get()) {
                suggestions.add(word);
            }
            if (suggestions.size() >= count) {
                return;
            }
            children.forEach((character, childNode) -> {
                String suggestionsWord = word + character;
                childNode.addSuggestions(suggestionsWord, suggestions, count);
            });
        }
    }

    private static final int MAX_SUGGESTION = 100;
    private static final int MAX_WORD_LENGTH = 35;
    private static final int MAX_PREFIX_LENGTH = 30;
    public static final int ZERO = 0;

    private final Node root = new Node();

    public void insert(String word) {
        if (word.length() > MAX_WORD_LENGTH) {
            return;
        }
        Node currentNode = root;
        for (char c : word.toCharArray()) {
            currentNode = currentNode.nextNode(c);
        }
        currentNode.settingWord();
    }

    public List<String> search(String prefix, int count) {
        if (prefix.length() > MAX_PREFIX_LENGTH) {
            prefix = prefix.substring(ZERO, MAX_PREFIX_LENGTH);
        }
        Node currentNode = root;
        for (char c : prefix.toCharArray()) {
            if (!currentNode.hasChildren(c)) {
                return List.of();
            }
            currentNode = currentNode.nextNode(c);
        }
        return findWords(prefix, currentNode, Math.min(MAX_SUGGESTION, count));
    }

    private List<String> findWords(String word, Node currentNode, int count) {
        List<String> suggestions = new ArrayList<>();
        currentNode.addSuggestions(word, suggestions, count);
        return suggestions;
    }
}
