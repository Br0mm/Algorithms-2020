package lesson6;

import kotlin.NotImplementedError;
import lesson6.impl.GraphBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {
    /**
     * Эйлеров цикл.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     *
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     */
    /*
    время O(Vertices + Edges)
    память S(Edges)
     */
    private static List<List<Graph.Vertex>> miniCycles;
    private static List<Graph.Edge> eulerLoop;

    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        miniCycles = new ArrayList<>();
        eulerLoop = new ArrayList<>();
        if (graph.getEdges().isEmpty()) return new ArrayList<>();
        Map<Graph.Vertex, Set<Graph.Vertex>> connections = new HashMap<>();
        for (Graph.Vertex vertex: graph.getVertices()) {
            if (graph.getNeighbors(vertex).size() % 2 == 1) return new ArrayList<>();
            connections.put(vertex, graph.getNeighbors(vertex));
        }
        Graph.Vertex currentPosition = graph.getVertices().iterator().next();
        int counterOfEdges = 0;
        List<Graph.Vertex> miniCycle = new ArrayList<>();
        miniCycle.add(currentPosition);
        while (counterOfEdges < graph.getEdges().size()) {
            Graph.Vertex nextPosition = connections.get(currentPosition).iterator().hasNext() ?
                    connections.get(currentPosition).iterator().next() : null;
            if (nextPosition != null) {
                miniCycle.add(nextPosition);
                connections.get(currentPosition).remove(nextPosition);
                connections.get(nextPosition).remove(currentPosition);
                counterOfEdges++;
                currentPosition = nextPosition;
            } else {
                if (!miniCycle.isEmpty()) miniCycles.add(miniCycle);
                currentPosition = miniCycle.get(1);
                miniCycle = new ArrayList<>();
                miniCycle.add(currentPosition);
            }
        }
        miniCycles.add(miniCycle);
        if (!miniCycles.isEmpty()) eulerLoopResultGenerator(0, graph);
        return eulerLoop;
    }

    private static void eulerLoopResultGenerator(int indexOfMiniCycle, Graph graph) {
        for (int i = 0; i < miniCycles.get(indexOfMiniCycle).size() - 1; i++) {
            Graph.Edge currentEdge = graph.getConnection(miniCycles.get(indexOfMiniCycle).get(i),
                    miniCycles.get(indexOfMiniCycle).get(i + 1));
            eulerLoop.add(currentEdge);
            if (indexOfMiniCycle + 1 < miniCycles.size() &&
                    miniCycles.get(indexOfMiniCycle).get(i + 1) == miniCycles.get(indexOfMiniCycle + 1).get(0))
                eulerLoopResultGenerator(indexOfMiniCycle + 1, graph);
        }
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     *
     * Дан связный граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ:
     *
     *      G    H
     *      |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     */
    /*
    время O(Vertices + Edges)
    память S(Vertices + Edges)
     */
    public static Graph minimumSpanningTree(Graph graph) {
        GraphBuilder spanningTree = new GraphBuilder();
        if (graph.getEdges().isEmpty()) return spanningTree.build();
        Set<Graph.Vertex> vertices = new HashSet<>();
        Queue<Graph.Vertex> verticesToVisit = new ArrayDeque<>();
        verticesToVisit.add(graph.getVertices().iterator().next());
        Graph.Vertex currentPosition;
        vertices.add(verticesToVisit.peek());
        while (!verticesToVisit.isEmpty()) {
            currentPosition = verticesToVisit.remove();
            for (Graph.Vertex vertex : graph.getNeighbors(currentPosition)) {
                if (!vertices.contains(vertex)) {
                    verticesToVisit.add(vertex);
                    spanningTree.addVertex(currentPosition.toString());
                    spanningTree.addVertex(vertex.toString());
                    spanningTree.addConnection(currentPosition, vertex, 1);
                    vertices.add(vertex);
                }
            }
        }
        return spanningTree.build();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     *
     * Дан граф без циклов (получатель), например
     *
     *      G -- H -- J
     *      |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     *
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     *
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     *
     * В данном случае ответ (A, E, F, D, G, J)
     *
     * Если на входе граф с циклами, бросить IllegalArgumentException
     *
     * Эта задача может быть зачтена за пятый и шестой урок одновременно
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Наидлиннейший простой путь.
     * Сложная
     *
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     */
    public static Path longestSimplePath(Graph graph) {
        throw new NotImplementedError();
    }


    /**
     * Балда
     * Сложная
     *
     * Задача хоть и не использует граф напрямую, но решение базируется на тех же алгоритмах -
     * поэтому задача присутствует в этом разделе
     *
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     *
     * И Т Ы Н
     * К Р А Н
     * А К В А
     *
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     *
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     *
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     *
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) {
        throw new NotImplementedError();
    }
}
