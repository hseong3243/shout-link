package com.seong.shoutlink.domain.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

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

        public Node getOrCreateNextNode(char c) {
            return children.computeIfAbsent(c, key -> new Node());
        }

        public Node getNextNode(char c) {
            return children.get(c);
        }

        public void addSuggestions(StringBuilder word, List<String> suggestions, int count) {
            if (isWord.get()) {
                suggestions.add(word.toString());
            }
            if (suggestions.size() >= count) {
                return;
            }
            children.forEach((character, childNode) -> {
                word.append(character);
                childNode.addSuggestions(word, suggestions, count);
                word.deleteCharAt(word.length() - 1);
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
            currentNode = currentNode.getOrCreateNextNode(c);
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
            currentNode = currentNode.getNextNode(c);
        }
        return findWords(prefix, currentNode, Math.min(MAX_SUGGESTION, count));
    }

    private List<String> findWords(String word, Node currentNode, int count) {
        List<String> suggestions = new ArrayList<>();
        currentNode.addSuggestions(new StringBuilder(word), suggestions, count);
        return suggestions;
    }
}
