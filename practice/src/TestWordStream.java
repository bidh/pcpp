import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.cert.CollectionCertStoreParameters;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestWordStream {
    public static void main(String[] args) {
        String filename = "./resources/words";
        // Exercise 4.3
        //1
        //System.out.println(readWords(filename).count());
        //2
        //readWords(filename).forEach(System.out::println);
        //3
        //readWords(filename).filter(x->x.length()>=22).forEach(System.out::println);
        //4
        //readWords(filename).filter(x->x.length()>=22).limit(5).forEach(System.out::println);
        /*System.out.println(readWords(filename).
                filter(w -> w.length() >= 22).
                findAny().
                orElse("nothing found"));*/
        //5
        //readWords(filename).filter(x->isPalindrome(x)).forEach(System.out::println);
        //6
        /*
        double runningTime=0.0;
        Timer t=new Timer();
        readWords(filename).filter(x->isPalindrome(x)).forEach(System.out::println);
        runningTime=t.check();
        System.out.println(runningTime);*/
        //7
        //IntSummaryStatistics wordsLength=readWords(filename).mapToInt(x->x.length()).summaryStatistics();
        //System.out.format("Maximum: %d, Minimum: %d, Average %.2f ",wordsLength.getMax(),wordsLength.getMin(),wordsLength.getAverage());
        //8
        /*Map<Integer,List<String>> wordsGroup=readWords(filename).collect(
            Collectors.groupingBy(x->x.length()));
        wordsGroup.forEach((length,words)->System.out.println(length+":"+words));*/

        //challenge
        /*Map<Integer,Long> wordsGroup1=readWords(filename).collect(Collectors.groupingBy(
                x->x.length(),Collectors.counting()
        ));
        wordsGroup1.forEach((length,numbers)->System.out.println(length+":"+numbers));*/
        //9
        // Map<Character,Integer> letterMap=letters("PErsistent");
        //letterMap.forEach((c,i)->System.out.println(c+":"+i));
        //System.out.println(letterMap);
        //readWords(filename).limit(100).map(x->letters(x)).forEach(System.out::println);
        //another approach
        //prints all map of words
        /*List<Map<Character,Integer>> wordsMap=readWords(filename)
                .limit(100)
                .map(x->letters(x))
                .collect(Collectors.toList());
        wordsMap.forEach(words->
                System.out.println(words)
        );*/
        //10

        /*OptionalInt countE=readWords(filename)
                .map(x->letters(x))
                .filter(x->x.containsKey('e'))
                .mapToInt(x->x.get('e'))
                .reduce((a,b)-> a+b);
        System.out.println(countE.getAsInt());*/

        
    }

    public static Stream<String> readWords(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            int lines = 0;
            String readLine="";
            List<String> words=new ArrayList<>();
            while ((readLine=reader.readLine()) != null){
                words.add(readLine);
                lines++;
            }
            reader.close();
            Stream<String> wordsStream=words.stream();
            return wordsStream;
        } catch (IOException exn) {
            return Stream.<String>empty();
        }
    }
    public static boolean isPalindrome(String s) {
        // TO DO: Implement properly
        boolean flag=false;
        int i=0;
        char[] c=s.toCharArray();
        if(c.length<2){
            return true;
        }
        String newS="";
        if(c[i]!=c[c.length-1]){
            return false;
        }
        for(int j=i+1;j<c.length-1;j++){
            newS+=c[j];
        }
        return isPalindrome(newS);
    }

    public static Map<Character,Integer> letters(String s) {
        String ss=s.toLowerCase();
        Map<Character,Integer> res = new TreeMap<>();
        char[] chars=ss.toCharArray();
        for(int i=0;i<chars.length;i++){
            if(!res.containsKey(chars[i])){
                res.put(chars[i],1);
            }else{
                int newV=res.get(chars[i])+1;
                res.put(chars[i],newV);
            }
        }
        return res;
    }
    static class Timer {
        private long start, spent = 0;
        public Timer() { play(); }
        public double check() { return (System.nanoTime()-start+spent)/1e9; }
        public void pause() { spent += System.nanoTime()-start; }
        public void play() { start = System.nanoTime(); }
    }
}
