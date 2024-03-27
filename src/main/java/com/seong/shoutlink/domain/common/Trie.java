package com.seong.shoutlink.domain.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class Trie {

    @Getter
    static class Node {

        private final char c;
        private final Map<Character, Node> children = new HashMap<>();
        private boolean isWord;

        public Node(char c) {
            this.c = c;
        }

        public void setWord(boolean word) {
            isWord = word;
        }

        public boolean hasChildren(char c) {
            return children.containsKey(c);
        }

        public boolean isWord() {
            return isWord;
        }

        public void addSuggestions(String word, List<String> suggestions, int count) {
            children.forEach((character, childNode) -> {
                String suggestionsWord = word + character;
                if (childNode.isWord()) {
                    suggestions.add(suggestionsWord);
                }
                if (suggestions.size() >= count) {
                    return;
                }
                childNode.addSuggestions(suggestionsWord, suggestions, count);
            });
        }
    }

    private static final int MAX_SUGGESTION = 100;
    private static final int MAX_WORD_LENGTH = 35;
    private static final int MAX_PREFIX_LENGTH = 30;
    public static final int ZERO = 0;

    private final Node root = new Node(' ');

    public synchronized void insert(String word) {
        if(word.length() > MAX_WORD_LENGTH) {
            return;
        }

        Node currentNode = root;
        for (char c : word.toCharArray()) {
            Map<Character, Node> children = currentNode.getChildren();
            currentNode = children.getOrDefault(c, new Node(c));
            children.put(c, currentNode);
        }
        currentNode.setWord(true);
    }

    public List<String> search(String prefix, int count) {
        if(prefix.length() > MAX_PREFIX_LENGTH) {
            prefix = prefix.substring(ZERO, MAX_PREFIX_LENGTH);
        }
        Node currentNode = root;
        for (char c : prefix.toCharArray()) {
            if (!currentNode.hasChildren(c)) {
                return List.of();
            }
            Map<Character, Node> children = currentNode.getChildren();
            currentNode = children.get(c);
        }
        return findWords(prefix, currentNode, Math.min(MAX_SUGGESTION, count));
    }

    private List<String> findWords(String word, Node currentNode, int count) {
        List<String> suggestions = new ArrayList<>();
        if (currentNode.isWord()) {
            suggestions.add(word);
        }
        currentNode.addSuggestions(word, suggestions, count);
        return suggestions;
    }
}
