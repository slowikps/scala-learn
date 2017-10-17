package learn.plain.algos;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PlayingWithJAVACollections {

    public static void main(String[] args) {
        System.out.println("Start");

        Pattern photoEntryPattern = Pattern.compile("(?mi)^(.+)[.](jpg|png|jpeg),\\s+ (.+),\\s+(.*)$");
        Matcher matcher = photoEntryPattern.matcher("photo.jpg, Warsaw, 2013-09-05 14:08:15");
        System.out.println(matcher.matches());



        List<String> input = Arrays.asList("A", "B", "C", "D", "A", "C");

        Map<String, Integer> res = new HashMap<>();
        for (String letter: input) {
            Integer count = res.get(letter);
            if(count == null) {
                count = 0;
            }
            res.put(letter, count + 1);
        }

        System.out.println("Res in loop: " + res);

        System.out.println(
                input.stream().collect(
                        Collectors.groupingBy(
                                Function.identity(),
                                Collectors.counting()
                        )
                )
        );

        System.out.println(
                input.stream().collect(
                        Collectors.groupingBy(Function.identity(),
                                Collectors.counting())
                )
        );

        System.out.println(
                input.stream().collect(
                        Collectors.groupingBy(Function.identity())
                )
        );

        System.out.println("End");
    }
}
