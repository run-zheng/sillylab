package com.sillylab.lambda.master;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

public class LambdaDemo2 {
    public static void main(String[] args) {
        //在lambda中再次声明一个局部变量是非法的

        //但是可以在函数体中引入新的名称
        IntUnaryOperator iuo = i -> {int j = 3; return i + j; };
        //lambda参数与lambda体局部声明可以隐藏字段名
        //lambda声明的块，this和super与外围的含义一致
        new Hello().r1.run();
        new Hello().r2.run();
        printLine();
        //lambda引用自身的问题：如果名字在作用域中，那么lambda就可以引用自身
        //    但是初始化器中的前向引用限制规则导致lambda变量无法初始化
        int calc = new Factorial().calc(10);
        System.out.println(calc);
        printLine();

        //6种上下文可以提供恰当的目标类型
        //- 方法或构造器参数
        //- 变量声明与赋值
        Comparator<String> comparator = (String s1, String s2) -> s1.compareToIgnoreCase(s2);
        IntBinaryOperator[] calculatorOps = new IntBinaryOperator[]{
                (x, y) -> x + y,
                (x, y) -> x - y,
                (x, y) -> x * y,
                (x, y) -> x / y
        };

        //- 返回语句
        //- lambda表达式体
        Callable<Runnable> c = () -> () -> System.out.println("hi");
        //- 三元表达式
        boolean flag = true;
        Callable<Integer> t = flag ? (() -> 23 ):(() -> 42);
        //- 类型转换表达式
        Object sl = (Supplier)() -> "hi";
        Object cl = (Callable)() -> "hi";

        //静态方法引用
        Integer[] integers = new Integer[]{5, 4, 3, 2, 1};
        Arrays.sort(integers, (x, y) -> Integer.compareUnsigned(x, y));
        Arrays.sort(integers, Integer::compareUnsigned);
        //实例方法绑定
        List<Point> pointList = Arrays.asList(new Point(1, 2), new Point(2, 3));
        pointList.forEach(p -> System.out.println(p));
        pointList.forEach(System.out::println);
        printLine();
        //未绑定方法引用
        Comparator<Point> pComparator = Comparator.comparing(p -> p.getX());
        Comparator<Point> pComparator2 = Comparator.comparing(Point::getX);

        TreeMap<String, String>  map = new TreeMap<>();
        map.put("alpha","X");
        map.put("bravo", "Y");
        map.put("charlie", "2");
        String str = "alpha-bravo-charlie";
        map.replaceAll(str::replace);
        System.out.println(map);
        printLine();
        map.replaceAll(String::concat);
        System.out.println(map);
        printLine();
        //构造器引用

    }
    //6种上下文可以提供恰当的目标类型：返回语句
    public static Runnable returnDatePrinter(){
        return () -> System.out.println(new Date());
    }

    public static void printLine(){
        System.out.println("---------------------------------------------");
    }

    /**
     * lambda引用自身的问题：如果名字在作用域中，那么lambda就可以引用自身
     *   如下声明一个递归定义的lambda
     */
    public static class Factorial {
        IntUnaryOperator fact ;

        public Factorial() {
            fact = i -> i == 0 ? 1 : i * fact.applyAsInt(i -1);
        }

        public int calc(int v){
            return fact.applyAsInt(v);
        }
    }

    /**
     * lambda声明的块，this和super与外围的含义一致
     */
    public static class Super {
        @Override
        public String toString() {
            return "Hello super!";
        }
    }
    /**
     * lambda声明的块，this和super与外围的含义一致
     */
    public static class Hello extends Super{
        Runnable r1 = () -> {
            System.out.println(this.toString());
        };
        Runnable r2 = () -> {
            System.out.println(super.toString());
        };

        @Override
        public String toString() {
            return "Hello world!";
        }
    }



    /**
     * lambda参数与lambda体局部声明可以隐藏字段名
     */
    public static class Foo {
        Object i, j ;
        IntUnaryOperator iuo = i -> {int j = 3; return i + j; };
    }

    /**
     * 在lambda中再次声明一个局部变量是非法的
     */
    public static void foo() {
        final int i = 2;
        Runnable r = () -> {
        //    int i = 3;   //illegal
        };
    }

}
