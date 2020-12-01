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
        Node parent = null;
        String value = null;
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
        StringBuilder nodeValue = new StringBuilder();
        for (char character : withZero(element).toCharArray()) {
            nodeValue.append(character);
            Node child = current.children.get(character);
            if (child != null) {
                current = child;
            } else {
                modified = true;
                Node newChild = new Node();
                current.children.put(character, newChild);
                newChild.parent = current;
                newChild.value = nodeValue.toString();
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
            removeEmptyBranches(current);
            return true;
        }
        return false;
    }

    private void removeEmptyBranches(Node current) {
        if (current.children.size() == 0 && current != root) {
            current.parent.children.remove(current.value.charAt(current.value.length() - 1));
            removeEmptyBranches(current.parent);
        }
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
        private Node previousNode;
        private int elementsFound;
        private Integer numberOfElements;
        private Node startNode;
        private Node lastNext;

        private TrieIterator() {
            if (root == null) return;
            elementsFound = 0;
            numberOfElements = size();
        }

        private Node findLeftest(Node current) {
            if (current.children.size() != 0) {
                for (Map.Entry<Character, Node> entry : current.children.entrySet()) {
                    if (entry.getKey() == (char) 0) return entry.getValue();
                    else return findLeftest(entry.getValue());
                }
            }
            return current;
        }

        /*
        время O(1)
        память O(1)
         */
        @Override
        public boolean hasNext() {
            return elementsFound < numberOfElements;
        }

        /*
        время O(WordLength)
        память O(1)
         */
        @Override
        public String next() {
            if (numberOfElements == null || elementsFound == numberOfElements) throw new NoSuchElementException();
            elementsFound++;
            if (startNode == null) {
                startNode = findLeftest(root);
                lastNext = startNode;
                return startNode.parent.value;
            }
            previousNode = startNode;
            boolean visited = true;
            while (visited || startNode != lastNext) {
                lastNext = startNode;
                visited = true;
                for (Map.Entry<Character, Node> entry : lastNext.parent.children.entrySet()) {
                    if (!visited) {
                        startNode = findLeftest(entry.getValue());
                        if (startNode.value.charAt(startNode.value.length() - 1) == (char) 0) {
                            lastNext = startNode;
                            return startNode.parent.value;
                        }
                    } else if (entry.getKey() == startNode.value.charAt(startNode.value.length() - 1)) visited = false;
                }
                startNode = lastNext.parent;
            }
            return lastNext.value;
        }

        /*
        время O(1)
        память O(1)
         */
        @Override
        public void remove() {
            if (lastNext == null) throw new IllegalStateException();
            lastNext.parent.children.remove((char) 0);
            removeEmptyBranches(lastNext.parent);
            startNode = previousNode;
            size--;
            lastNext = null;
        }

    }
}