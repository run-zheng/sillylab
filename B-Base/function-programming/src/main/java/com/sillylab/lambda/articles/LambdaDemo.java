package com.sillylab.lambda.articles;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * https://www.cnblogs.com/CarpenterLee/p/5936664.html
 */
public class LambdaDemo {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread -> "+Thread.currentThread().getName()+" run");
            }
        }).start();

        //lambda
        new Thread(() -> System.out.println("Thread -> "+Thread.currentThread().getName()+" run"))
                .start();

        //lambda书写形式
        Runnable run = () -> System.out.println("Hello world!");
        ActionListener listener = event -> System.out.println("Button clicked!");
        Runnable multi = () -> {
            System.out.println("Hello");
            System.out.println("world!");
        };
        BinaryOperator<Long> add = (a, b) -> a + b;

        //JDK7的写法
        List<String> list = Arrays.asList("1one", "two", "three", "4four");
        for(String str: list){
            if(Character.isDigit(str.charAt(0))){
                System.out.println(str);
            }
        }
        //lambda的写法，找出首字母是数字的打印
        list.stream().filter(str -> Character.isDigit(str.charAt(0)))
                .forEach(str -> System.out.println(str));
        //找出首字母不是数字的，转换成大写，用Set收集
        Set<String> newSet = list.stream()
                .filter(str -> !Character.isDigit(str.charAt(0))) //过滤操作
                .map(String::toUpperCase) //方法引用  映射操作
                .collect(Collectors.toSet()); //Collector收集器
    }
}
