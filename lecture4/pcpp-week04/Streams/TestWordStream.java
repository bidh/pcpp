
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.BinaryOperator;
import java.util.function.DoubleSupplier;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestWordStream {
    public static void main(String[] args) {
        final String filename = "/usr/share/dict/words";

//TODO: uncomment exercises as you proceed checking them

        // exercise 4.3 - 1
//        System.out.println("count=" + readWords(filename).count());

        // exercise 4.3 - 2
//        readWords(filename).limit(100).forEach(System.out::println);

        // exercise 4.3 - 3
//        readWords(filename).filter(w -> w.length() >= 22).forEach(System.out::println);

        // exercise 4.3 - 4
//        System.out.println(readWords(filename).
//                filter(w -> w.length() >= 22).
//                findAny().
//                orElse("nothing found"));
//
        // exercise 4.3 - 5
//        System.out.println("sequential");
//        readWords(filename).filter(w -> isPalindrome(w)).forEach(System.out::println);
//
//         exercise 4.3 - 6
//        System.out.println("parallel");
//        readWords(filename).parallel().filter(w -> isPalindrome(w)).forEach(System.out::println);
//
//         exercise 4.3 - 7
//        IntSummaryStatistics is = readWords(filename).mapToInt(w -> w.length()).summaryStatistics();
//        System.out.printf("max=%d, min=%d, avg=%.4f", is.getMax(), is.getMin(), is.getAverage());
//
//         exercise 4.3 - 8
//        Map<Integer, List<String>> groups = readWords(filename).
//                collect(Collectors.groupingBy(w -> w.length()));
//        groups.forEach((length, list) -> System.out.println(length + " " + list));
//
//         exercise 4.3 - 8 : optional Challenge
//        Map<Integer, Long> groupSizes = readWords(filename).
//                collect(Collectors.groupingBy(w -> w.length(), Collectors.counting()));
//        groupSizes.forEach((length, groupSize) -> System.out.println(length + " " + groupSize));
//

//        exercise 4.3 - 9
//        Stream<Map<Character, Integer>> mapOfWords = readWords(filename).map(s -> letters(s));
//        mapOfWords.limit(100).forEach(word -> {
//            System.out.println("");
//            // for each character
//            word.forEach((c, i) ->
//                    System.out.print(c + "=" + i + " ")
//            );
//        });

//        exercise 4.3 - 10
//        int countOfE = readWords(filename).map(s -> letters(s)).
//                filter(m1 -> m1.containsKey('e')).
//                mapToInt(m2 -> m2.get('e')).
//                reduce((a, b) -> a + b).
//                orElse(0);
//
//        System.out.println(countOfE);

        //        exercise 4.3 - 11
//        Map<Map<Character, Integer>, List<String>> anagrams = readWords(filename).
//                collect(Collectors.groupingBy(w -> letters(w)));
//
//        int setsOfAnagramsCount = 0;
//        for (Entry<Map<Character, Integer>, List<String>> entry : anagrams.entrySet()) {
//            if (entry.getValue().size() > 1) {
//                System.out.println(entry.getValue());
//                setsOfAnagramsCount++;
//            }
//        }
//        System.out.println(setsOfAnagramsCount);


//                exercise 4.3 - 12 & 13
//        Map<Map<Character, Integer>, List<String>> anagrams = readWords(filename).
//                parallel().
//                collect(Collectors.groupingByConcurrent(w -> letters(w)));
//
//        int setsOfAnagramsCount = 0;
//        for (Entry<Map<Character, Integer>, List<String>> entry : anagrams.entrySet()) {
//            if (entry.getValue().size() > 1) {
//                System.out.println(entry.getValue());
//                setsOfAnagramsCount++;
//            }
//        }
//        System.out.println(setsOfAnagramsCount);
//

//        TODO: Exercise 4.4 -> I didn't know if I should create another java file
//        TODO: so I solved them here

//        exercise 4.4 - 1
//        DoubleStream ds = IntStream.range(1, 1_000_000_000).mapToDouble(i -> 1.0 / i);
//        System.out.printf("Sum = %20.16f%n", ds.sum());

//        exercise 4.4 - 2
//        DoubleStream ds1 = IntStream.range(1, 1_000_000_000).mapToDouble(i -> 1.0 / i);
//        System.out.printf("Sum = %20.16f%n", ds1.parallel().sum());

//        exercise 4.4 - 3
//        double sum = 0.0;
//        for (int i = 1; i < 1_000_000_000; i++) {
//            sum += 1.0 / i;
//        }
//        System.out.println(sum);

//      exercise 4.4 - 4
//        DoubleSupplier supplier = new DoubleSupplier() {
//            private int current = 1;
//
//            public double getAsDouble() {
//                return 1.0 / current++;
//            }
//        };
//        DoubleStream ds4 = DoubleStream.generate(supplier);
//        System.out.println(ds4.limit(1_000_000_000).sum());

//      exercise 4.4 - 5
//      use the DoubleStream from exercise 3.4 - 4
//        System.out.println(ds4.limit(1_000_000_000).parallel().sum());

    }

    public static Stream<String> readWords(String filename) {
        try {
            String filePath = new File("").getAbsolutePath();
            BufferedReader reader = new BufferedReader(new FileReader(filePath + filename));
            return reader.lines();
        } catch (IOException exn) {
            return Stream.<String>empty();
        }
    }

    public static boolean isPalindrome(String s) {
        String reversed = new StringBuffer(s).reverse().toString();
        return reversed.equals(s);
    }

    public static Map<Character, Integer> letters(String s) {
        Map<Character, Integer> res = new TreeMap<>();
        for (char c : s.toLowerCase().toCharArray()) {
            if (res.get(c) != null)
                res.put(c, (res.get(c) + 1));
            else
                res.put(c, 1);
        }

        return res;
    }
}
