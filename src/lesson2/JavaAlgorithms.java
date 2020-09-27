package lesson2;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.io.*;

@SuppressWarnings("unused")
public class JavaAlgorithms {
    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     *
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     *
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     *
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
     /*
     время: O(N)
     память: S(1)
     */
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) throws IOException {
        Pair<Integer, Integer> currentBuy;
        Pair<Integer, Integer> probableBuy;
        Pair<Integer, Integer> currentSell;
        int counter = 1;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputName)))) {
            String share = reader.readLine();
            if (!share.matches("^\\d+$")) throw new IllegalArgumentException();
            currentBuy = new Pair<>(1,Integer.parseInt(share));
            probableBuy = new Pair<>(1, Integer.parseInt(share));
            currentSell = new Pair<>(1, Integer.parseInt(share));
            share = reader.readLine();
            while (share != null) {
                if (!share.matches("^\\d+$")) throw new IllegalArgumentException();
                int shareCost = Integer.parseInt(share);
                counter++;

                if (shareCost - currentBuy.getSecond() > currentSell.getSecond() - currentBuy.getSecond())
                    currentSell = new Pair<>(counter, shareCost);
                if (shareCost - probableBuy.getSecond() > currentSell.getSecond() - currentBuy.getSecond()) {
                    currentBuy = new Pair<>(probableBuy.getFirst(), probableBuy.getSecond());
                    currentSell = new Pair<>(counter, shareCost);
                }
                if (probableBuy.getSecond() > shareCost) probableBuy = new Pair<>(counter, shareCost);
                share = reader.readLine();
            }
        }

        return new Pair<>(currentBuy.getFirst(), currentSell.getFirst());
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     *
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     *
     * 1 2 3
     * 8   4
     * 7 6 5
     *
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     *
     * 1 2 3
     * 8   4
     * 7 6 х
     *
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     *
     * 1 х 3
     * 8   4
     * 7 6 Х
     *
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     *
     * 1 Х 3
     * х   4
     * 7 6 Х
     *
     * 1 Х 3
     * Х   4
     * х 6 Х
     *
     * х Х 3
     * Х   4
     * Х 6 Х
     *
     * Х Х 3
     * Х   х
     * Х 6 Х
     *
     * Х Х 3
     * Х   Х
     * Х х Х
     *
     * Общий комментарий: решение из Википедии для этой задачи принимается,
     * но приветствуется попытка решить её самостоятельно.
     */
    static public int josephTask(int menNumber, int choiceInterval) {
        throw new NotImplementedError();
    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     *
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     */
    /*
    N - длина первой строки, M - длина второй строки
     время: O(N*M)
     память: S(M)
     */
    static public String longestCommonSubstring(String first, String second) {
        int minLength = Math.min(first.length(), second.length());
        int[][] matrix = new int[2][second.length()];
        int maxI = -1;
        int max = -1;
        for (int i = 0; i < first.length(); i++) {
            for (int j = 0; j < second.length(); j++) {
                if (first.charAt(i) == second.charAt(j)) {
                    if (i > 0 && j > 0) matrix[1][j] = matrix[0][j - 1] + 1;
                    else matrix[1][j] = 1;
                    if (matrix[1][j] > max) {
                        max = matrix[1][j];
                        maxI = i;
                    }
                } else matrix[1][j] = 0;
            }
            matrix[0] =  matrix[1].clone();
        }
        String result = "";
        if (max != -1) result = first.substring(maxI - max + 1, maxI + 1);
        return  result;
    }

    /**
     * Число простых чисел в интервале
     * Простая
     *
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     *
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     */
    /*
    время: O(N*log(log(N)))
     память: S(N)
     */
    static public int calcPrimesNumber(int limit) {
        if (limit < 2) return 0;
        int simpleNumbers = 0;
        boolean[] numbers = new boolean[limit + 1];
        for (int i = 0; i <= limit; i++)
            numbers[i] = true;
        for (int i = 2; i <= limit; i++) {
            if (numbers[i]) {
                simpleNumbers++;
                for (int j = i * 2; j <= limit; j += i) {
                    numbers[j] = false;
                }
            }
        }
        return simpleNumbers;
    }
}
