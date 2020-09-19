package lesson1;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     * <p>
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     * <p>
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     * <p>
     * Пример:
     * <p>
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     * <p>
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     * <p>
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    /*
    время: O(N*LogN)
    память: S(N)
     */
    static public void sortTimes(String inputName, String outputName) throws IOException {
        ArrayList<String> amTimes = new ArrayList<>();
        ArrayList<String> pmTimes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputName)))) {
            String time = reader.readLine();
            while (time != null) {
                if (!time.matches("^(0[1-9]|1[0-2]):([0-5]\\d):([0-5]\\d) [PA]M$"))
                    throw new IllegalArgumentException();
                if (time.startsWith("12")) time = time.replaceFirst("12", "00");
                if (time.endsWith("PM")) pmTimes.add(time);
                else amTimes.add(time);
                time = reader.readLine();
            }
        }
        Collections.sort(amTimes);
        Collections.sort(pmTimes);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputName)))) {
            for (String time : amTimes) {
                if (time.startsWith("00")) time = time.replaceFirst("00", "12");
                writer.write(time + System.getProperty("line.separator"));
            }
            for (String time : pmTimes) {
                if (time.startsWith("00")) time = time.replaceFirst("00", "12");
                writer.write(time + System.getProperty("line.separator"));
            }
        }
    }

    /**
     * Сортировка адресов
     * <p>
     * Средняя
     * <p>
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     * <p>
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     * <p>
     * Людей в городе может быть до миллиона.
     * <p>
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     * <p>
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    /*
    время: O(N*LogN)
    память: S(N)
     */
    static public void sortAddresses(String inputName, String outputName) throws IOException {
        Map<String, ArrayList<String>> addressBook = new TreeMap<>(
                (o1, o2) -> {
                    String[] address1 = o1.split(" ");
                    String[] address2 = o2.split(" ");
                    int result;
                    if (!address1[0].equals(address2[0])) result = o1.compareTo(o2);
                    else result = Integer.valueOf(address1[1]).compareTo(Integer.valueOf(address2[1]));
                    return result;
                });
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputName), StandardCharsets.UTF_8))) {
            String address = reader.readLine();
            while (address != null) {
                if (!address.matches("^[\\wа-яА-ЯЁё]+ [\\wа-яА-ЯЁё]+ - [\\wа-яА-ЯЁё\\-]+ \\d+$"))
                    throw new IllegalArgumentException();

                String[] parts = address.split(" - ");
                if (!addressBook.containsKey(parts[1])) addressBook.put(parts[1], new ArrayList<>());
                addressBook.get(parts[1]).add(parts[0]);
                address = reader.readLine();
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputName), StandardCharsets.UTF_8))) {
            addressBook.forEach((k, v) -> {
                Collections.sort(v);
                try {
                    writer.write(k + " - " + v.get(0));
                    for (int i = 1; i < v.size(); i++) {
                        writer.write(", " + v.get(i));
                    }
                    writer.write(System.getProperty("line.separator"));
                } catch (IOException ignored) {
                }
            });
        }
    }

    /**
     * Сортировка температур
     * <p>
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     * <p>
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     * <p>
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     * <p>
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     * <p>
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */
    /*
    время: O(N*LogN)
    память: S(N)
     */
    static public void sortTemperatures(String inputName, String outputName) throws IOException {
        ArrayList<Double> belowZero = new ArrayList<>();
        ArrayList<Double> overZero = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputName)))) {
            String temperature = reader.readLine();
            while (temperature != null) {
                String[] parts = temperature.split("\\.");
                if (temperature.startsWith("-")) belowZero.add(Double.parseDouble(temperature));
                else overZero.add(Double.parseDouble(temperature));
                temperature = reader.readLine();
            }
        }

        Collections.sort(belowZero);
        Collections.sort(overZero);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputName)))) {
            for (Double temperature : belowZero) {
                writer.write(temperature + System.getProperty("line.separator"));
            }
            for (Double temperature : overZero) {
                writer.write(temperature + System.getProperty("line.separator"));
            }
        }
    }

    /**
     * Сортировка последовательности
     * <p>
     * Средняя
     * (Задача взята с сайта acmp.ru)
     * <p>
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     * <p>
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     * <p>
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     * <p>
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    /*
    время: O(N)
    память: S(N^2)
     */
    static public void sortSequence(String inputName, String outputName) throws IOException {
        Map<Integer, Integer> dictionary = new HashMap<>();
        ArrayList<Integer> numbers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputName)))) {
            String number = reader.readLine();
            while (number != null) {
                Integer currentNumber = Integer.parseInt(number);
                if (!dictionary.containsKey(currentNumber)) dictionary.put(currentNumber, 1);
                else dictionary.put(currentNumber, dictionary.get(currentNumber) + 1);
                numbers.add(currentNumber);
                number = reader.readLine();
            }
        }
        int min = 0;
        for (Map.Entry<Integer, Integer> entry : dictionary.entrySet()) {
            if (min == 0) min = entry.getKey();
            else if (dictionary.get(min) < entry.getValue()) min = entry.getKey();
            else if (dictionary.get(min).equals(entry.getValue()) && min > entry.getKey()) min = entry.getKey();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputName)))) {
            for (Integer number : numbers) {
                if (number != min) writer.write(number + System.getProperty("line.separator"));
            }
            for (int i = 0; i < dictionary.get(min); i++) {
                writer.write(min + System.getProperty("line.separator"));
            }
        }
    }

    /**
     * Соединить два отсортированных массива в один
     * <p>
     * Простая
     * <p>
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     * <p>
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     * <p>
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        throw new NotImplementedError();
    }
}
