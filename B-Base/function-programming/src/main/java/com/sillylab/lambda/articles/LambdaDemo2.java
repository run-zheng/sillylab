package com.sillylab.lambda.articles;

import com.sillylab.lambda.Common;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * https://blog.csdn.net/y5492853/article/details/120938444
 */
public class LambdaDemo2 extends Common {
    public static void main(String[] args) {
        //函数式接口
        IntOperate add = (a, b) -> a + b ;
        IntOperate subtract = (a, b) -> a - b ;
        IntOperate multiply = (a, b) -> a * b ;
        IntOperate divide = (a, b) -> a / b ;
        System.out.println(operate(add, 2, 1));
        System.out.println(operate(subtract, 2, 1));
        System.out.println(operate(multiply, 2, 1));
        System.out.println(operate(divide, 2, 1));
        printLine();
        BinaryOperator<Integer> add2 = (a, b) -> a + b;
        BinaryOperator<Integer> subtract2 = (a, b) -> a - b;
        BinaryOperator<Integer> multiply2 = (a, b) -> a * b;
        BinaryOperator<Integer> divide2 = (a, b) -> a / b;
        //方法引用
        //构造方法引用
        Supplier<Person> supplier = Person::new;
        Supplier<Person> supplier2 = () -> new Person();
        //静态方法引用
        Consumer<Person> say = Person::say;
        Consumer<Person> say2 = person -> Person.say(person);
        //实例方法引用
        BiConsumer<Person, Person> personPersonBiConsumer = Person::equals;
        BiConsumer<Person, Person> personPersonBiConsumer2 = (inst, params) -> inst.equals(params);
        //对象方法引用
        Person person = new Person();
        Consumer<String> eat = person::eat;
        Consumer<String> eat2 = food -> person.eat(food);
    }

    public static class Person {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        public static void say(Person person){
            System.out.println(person.getName());
        }
        public void eat(String food){
            System.out.println("eat: " + food);
        }
    }


    public static int operate(IntOperate operate, int a, int b){
        return operate.operate(a, b);
    }

    @FunctionalInterface
    public interface IntOperate {
        int operate(int a, int b);
    }
}
