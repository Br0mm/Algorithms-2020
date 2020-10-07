package lesson4;

import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {

    private static class Node {
        Map<Character, Node> children = new LinkedHashMap<>();
    }

    private Node root = new Node();

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root.children.clear();
        size = 0;
    }

    private String withZero(String initial) {
        return initial + (char) 0;
    }

    @Nullable
    private Node findNode(String element) {
        Node current = root;
        for (char character : element.toCharArray()) {
            if (current == null) return null;
            current = current.children.get(character);
        }
        return current;
    }

    @Override
    public boolean contains(Object o) {
        String element = (String) o;
        return findNode(withZero(element)) != null;
    }

    @Override
    public boolean add(String element) {
        Node current = root;
        boolean modified = false;
        for (char character : withZero(element).toCharArray()) {
            Node child = current.children.get(character);
            if (child != null) {
                current = child;
            } else {
                modified = true;
                Node newChild = new Node();
                current.children.put(character, newChild);
                current = newChild;
            }
        }
        if (modified) {
            size++;
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        String element = (String) o;
        Node current = findNode(element);
        if (current == null) return false;
        if (current.children.remove((char) 0) != null) {
            size--;
            return true;
        }
        return false;
    }

    /**
     * Итератор для префиксного дерева
     * <p>
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     * <p>
     * Сложная
     */
    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new TrieIterator();
    }

    public class TrieIterator implements Iterator<String> {
        List<String> words = new ArrayList<>();

        private int index = 0;
        private String lastNext;

        private TrieIterator() {
            if (root == null)
                return;
            addToWords(root, "");
        }

        private void addToWords(Node parent, String partOfWord) {
            if (parent.children.size() != 0)
                parent.children.forEach((k, v) -> {
                    if (k == (char) 0) words.add(partOfWord);
                    else addToWords(v, partOfWord + k);
                });
        }

        /*
        время O(1)
        память O(1)
         */
        @Override
        public boolean hasNext() {
            return index < words.size();
        }

        /*
        время O(1)
        память O(1)
         */
        @Override
        public String next() {
            if (index == words.size()) throw new NoSuchElementException();
            String currentWord = words.get(index);
            index++;
            lastNext = currentWord;
            return lastNext;
        }

        /*
        время O(LogN)
        память O(1)
         */
        @Override
        public void remove() {
            if (lastNext == null) throw new IllegalStateException();
            Trie.this.remove(lastNext);
            lastNext = null;
        }

    }
}