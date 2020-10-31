package lesson5;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class OpenAddressingSet<T> extends AbstractSet<T> {

    enum Deleted {
        DELETED
    }

    private final int bits;

    private final int capacity;

    private final Object[] storage;

    private int size = 0;

    private int startingIndex(Object element) {
        return element.hashCode() & (0x7FFFFFFF >> (31 - bits));
    }

    public OpenAddressingSet(int bits) {
        if (bits < 2 || bits > 31) {
            throw new IllegalArgumentException();
        }
        this.bits = bits;
        capacity = 1 << bits;
        storage = new Object[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    @Override
    public boolean contains(Object o) {
        int index = startingIndex(o);
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o)) {
                return true;
            }
            index = (index + 1) % capacity;
            current = storage[index];
        }
        return false;
    }

    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     */
    @Override
    public boolean add(T t) {
        int startingIndex = startingIndex(t);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null && current != Deleted.DELETED) {
            if (current.equals(t)) {
                return false;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) {
                throw new IllegalStateException("Table is full");
            }
            current = storage[index];
        }
        storage[index] = t;
        size++;
        return true;
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблица, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     */
    /*
    A = n / m, где n = число элементов, m = размерность массива
    время O(1 / (1 - A))
    память O(1)
     */
    @Override
    public boolean remove(Object o) {
        if (!contains(o)) return false;
        int index = startingIndex(o);
        Object current = storage[index];
        while (!current.equals(o)) {
            index = (index + 1) % capacity;
            current = storage[index];
        }
        storage[index] = Deleted.DELETED;
        size--;
        return true;
    }

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     */
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new OpenAddressingSetIterator();
    }

    public class OpenAddressingSetIterator implements Iterator<T> {
        private int index = 0;
        private int elementsFound = 0;
        private int numberOfElements = size();
        private Object lastNext;

        /*
        время O(1)
        память O(1)
         */
        @Override
        public boolean hasNext() {
            return elementsFound < numberOfElements;
        }

        /*
        A = n / m, где n = число элементов, m = размерность массива
        время O(1 / A)
        память O(1)
         */
        @Override
        public T next() {
            if (elementsFound == numberOfElements) throw new NoSuchElementException();
            lastNext = null;
            while (lastNext == null || lastNext == Deleted.DELETED) {
                lastNext = storage[index];
                index++;
            }
            elementsFound++;
            return (T) lastNext;
        }

        /*
        время O(1)
        память O(1)
        */
        @Override
        public void remove() {
            if (lastNext == null) throw new IllegalStateException();
            storage[index - 1] = Deleted.DELETED;
            size--;
            lastNext = null;
        }
    }
}
