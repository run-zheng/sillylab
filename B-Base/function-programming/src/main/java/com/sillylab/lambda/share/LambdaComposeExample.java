package com.sillylab.lambda.share;

import com.sillylab.lambda.share.AppleExample.Apple;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

public class LambdaComposeExample {
    public static void main(String[] args) {
        //逆序比较
        Comparator<Apple> c = Comparator.comparing(Apple::getWeight).reversed();
        //比较连接器
        Comparator<Apple> c2 = Comparator.comparing(Apple::getWeight)
                .reversed()
                .thenComparing(Apple::getColor);
        //谓词复合
        Predicate<Apple> redApple = a -> "red".equals(a.getColor());
        Predicate<Apple> notRedApple = redApple.negate();
        Predicate<Apple> redAndHeavyApple = redApple.and(a -> a.getWeight() > 150);
        Predicate<Apple> redAndHeavyAppleOrGreen = redApple
                .and(a -> a.getWeight() > 150)
                .or(a -> "green".equals(a.getColor()));

        //函数复合
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        Function<Integer, Integer> h = f.andThen(g); // g(f(x))
        int result = h.apply(1); //4
        Function<Integer, Integer> h2 = f.compose(g); //f(g(x)
        int result2 = h2.apply(1); //3

    }
}
