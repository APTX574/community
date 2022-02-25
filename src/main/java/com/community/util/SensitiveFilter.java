package com.community.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aptx
 */
@Component
public class SensitiveFilter {

    @Value("${community.sensitive.file}")
    String file;

    TrieNode root;

    private static class TrieNode {

        private boolean isKeywordEnd = false;

        private Map<Character, TrieNode> children;

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        public TrieNode() {
            children = new HashMap<>();
        }

        public TrieNode getChild(Character ch) {
            if (children.containsKey(ch)) {
                return children.get(ch);
            }
            return null;
        }

        public TrieNode setChild(Character ch) {
            TrieNode trieNode = new TrieNode();
            children.put(ch, trieNode);
            return trieNode;
        }
    }

    @PostConstruct
    public void init() {
        TrieNode rootNode = new TrieNode();
        try (
                InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
        ) {
            char[] buffer = new char[512];
            int n;
            while ((n = reader.read(buffer)) != -1) {
                int i = 0;
                assert n > 0;
                //TODO 处理符号
                while (i < n) {
                    int j = i;
                    TrieNode p = rootNode;
                    while (j < n && buffer[j] != ' ') {
                        TrieNode child = p.getChild(buffer[j]);
                        if (child == null) {
                            child = p.setChild(buffer[j]);
                        }
                        p = child;
                        j++;
                    }
                    if (p != rootNode) {
                        p.isKeywordEnd = true;
                    }
                    i = j + 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        root = rootNode;
    }

    public void replace(StringBuffer sb) {
        int i = 0;
        int j ;
        int n = sb.length();
        TrieNode p;
        while (i < n) {
            p = root;
            j = i;
            while (j < n && p.getChild(sb.charAt(j)) != null) {
                p = p.getChild(sb.charAt(j));
                assert p != null;
                j++;
            }
            if (p.isKeywordEnd) {
                sb.replace(i, j, "*".repeat(Math.max(0, j - i)));
                i = j - 1;
            }
            i++;
        }
    }
}
