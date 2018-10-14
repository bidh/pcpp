import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class Main {
    public static boolean isMoreThanFifty(int n1, int n2) {
        return (n1 + n2) > 50;
    }
    public static List<Integer> findNumbers(List<Integer> l, BiPredicate<Integer, Integer> p) {
        List<Integer> newList = new ArrayList<>();
        for(Integer i : l) {
            if(p.test(i, i + 10)) {
                newList.add(i);
            }
        }
        return newList;
    }
    public static void main(String[] args)
    {
        /*Function<Double,String> hexfun;
		hexfun=Double::toHexString;
		int len1=hexfun.andThen(String::length).apply(123.2323235);
		System.out.println(len1);*/



        List<Integer> list = Arrays.asList(12,5,45,18,33,24,40);


        // Using an anonymous class
        System.out.println(findNumbers(list, new BiPredicate<Integer, Integer>() {
            public boolean test(Integer i1, Integer i2) {
                return Main.isMoreThanFifty(i1, i2);
            }
        }));

        // Using a lambda expression
        System.out.println(findNumbers(list, (i1, i2) -> Main.isMoreThanFifty(i1, i2)));

        // Using a method reference
        System.out.println(findNumbers(list, Main::isMoreThanFifty));
    }
}
