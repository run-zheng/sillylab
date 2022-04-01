package com.sillylab.lambda.share;

import com.sillylab.lambda.Common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class AppleExample extends Common {
    public static class Apple {
        private int weight ;
        private String color;

        public Apple(int weight, String color) {
            this.weight = weight;
            this.color = color;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
        @Override
        public String toString() {
            return "Apple{" +
                    "weight=" + weight +
                    ", color='" + color + '\'' +
                    '}';
        }
    }
    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(
                new Apple(80, "green"),
                new Apple(155, "green"),
                new Apple(120, "red"));
        //1、筛选绿苹果
        System.out.println(filterGreenApples(inventory));
        printLine("1、筛选绿苹果");
        //2、参数化颜色
        System.out.println(filterGreenApplesByColor(inventory, "green"));
        System.out.println(filterGreenApplesByColor(inventory, "red"));
        printLine("2、参数化颜色");
        //3、参数化所有属性做筛选
        System.out.println(filterApples(inventory, "green", -1, FilterFlag.COLOR));
        System.out.println(filterApples(inventory, "", 120, FilterFlag.WEIGHT));
        System.out.println(filterApples(inventory, "green", 80, FilterFlag.BOTH));
        printLine("3、参数化所有属性做筛选");
        //4、行为参数化  策略模式
        System.out.println(filterApples(inventory, new AppleGreenColorPredicate()));
        System.out.println(filterApples(inventory, new AppleHeavyWeightPredicate()));
        printLine("4、行为参数化  策略模式");
        //5、使用匿名内部类
        System.out.println(filterApples(inventory, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return "red".equals(apple.getColor()) && apple.getWeight() > 100;
            }
        }));
        printLine("5、使用匿名内部类");
        //6、使用函数式接口
        System.out.println(filterApplesByFunctionInterface(inventory, new Predicate<Apple>(){
            @Override
            public boolean test(Apple apple) {
                return  "red".equals(apple.getColor()) && apple.getWeight() > 100;
            }
        }));
        printLine("6、使用函数式接口");

        //7、使用lambda表达式
        System.out.println(filterApplesByFunctionInterface(inventory,
                (Apple apple) -> "red".equals(apple.getColor()) && apple.getWeight() > 100));
        printLine("7、使用lambda表达式");

        //8、进一步抽象，将List抽象
        System.out.println(filter(inventory, apple  -> "red".equals(apple.getColor()) && apple.getWeight() > 100));
        printLine("8、进一步抽象，将List抽象");
        //数据转换、结果集收集
    }

    //1、筛选绿苹果
    public static List<Apple> filterGreenApples(List<Apple> inventory){
        List<Apple> result = new ArrayList<>();
        for(Apple apple: inventory){
            if("green".equals(apple.getColor())){
                result.add(apple);
            }
        }
        return result;
    }

    //2、参数化颜色
    public static List<Apple> filterGreenApplesByColor(List<Apple> inventory, String color){
        List<Apple> result = new ArrayList<>();
        for(Apple apple: inventory){
            if(color.equals(apple.getColor())){
                result.add(apple);
            }
        }
        return result;
    }
    //3、参数化所有属性做筛选
    public enum FilterFlag {
        COLOR, WEIGHT, BOTH
    }
    public static List<Apple> filterApples(List<Apple> inventory, String color, int weight, FilterFlag flag){
        List<Apple> result = new ArrayList<>();
        for(Apple apple: inventory){
            switch (flag) {
                case COLOR:
                    if(color.equals(apple.getColor())){
                        result.add(apple);
                    }
                    break;
                case WEIGHT:
                    if(weight == apple.getWeight()){
                        result.add(apple);
                    }
                    break;
                case BOTH:
                default:
                    if(color.equals(apple.getColor()) && weight == apple.getWeight()){
                        result.add(apple);
                    }
            }
        }
        return result;
    }
    //4、行为参数化  策略模式
    public interface ApplePredicate {
        boolean test(Apple apple);
    }
    public static class AppleHeavyWeightPredicate implements ApplePredicate{
        @Override
        public boolean test(Apple apple) {
            return apple.getWeight() > 150;
        }
    }
    public static class AppleGreenColorPredicate implements ApplePredicate {
        @Override
        public boolean test(Apple apple) {
            return "green".equals(apple.getColor());
        }
    }
    public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p){
        List<Apple> result = new ArrayList<>();
        for(Apple apple: inventory){
            if(p.test(apple)){
                result.add(apple);
            }
        }
        return result;
    }
    //6、使用函数式接口
    public static List<Apple> filterApplesByFunctionInterface(List<Apple> inventory, Predicate<Apple> p){
        List<Apple> result = new ArrayList<>();
        for(Apple apple: inventory){
            if(p.test(apple)){
                result.add(apple);
            }
        }
        return result;
    }
    //8、进一步抽象，将List抽象
    public static <T> List<T> filter(List<T> list, Predicate<T> p){
        List<T> result = new ArrayList<>();
        for(T e: list){
            if(p.test(e)){
                result.add(e);
            }
        }
        return result;
    }
}
