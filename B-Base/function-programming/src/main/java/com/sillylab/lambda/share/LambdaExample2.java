package com.sillylab.lambda.share;

import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LambdaExample2 {
    public static void main(String[] args) {
        Runnable r = () -> System.out.println();
        Supplier<String> supplier = () -> "Hello world";
        Supplier<String> supplier2 = () ->{ return "Hello world"; };
        Predicate<Integer> predicate = i -> i > 5;
        Predicate<Integer> predicate2 = (Integer i)  -> i > 5;
        Function<Integer, String> function = i -> String.valueOf(i);

        //this无效
        //Runnable r = () -> System.out.println(this);
        System.out.println(new LambdaRef().ref("Hello").apply("world"));
    }

    public static class LambdaRef {
        private String name = "LambdaRef";

        public <T> Function<T, String> ref(T t){
            return (y) -> new StringJoiner(" ")
                    .add(this.name)
                    .add(String.valueOf(t))
                    .add(String.valueOf(y))
                    .toString();
        }
    }
}
