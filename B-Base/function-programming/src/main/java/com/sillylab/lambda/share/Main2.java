package com.sillylab.lambda.share;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class Main2 {

    public static void main(String[] args) {
        Integer add = Operator.<Integer>from((x, y) -> x + y)
                .apply(1)
                .apply(2);
        System.out.println(add); //3

        UnaryOperator<Integer> addFromOne = Operator.<Integer>from((x, y) -> x + y)
                .apply(1);

        System.out.println(addFromOne.apply(3)); //4
        System.out.println(addFromOne.apply(4)); //5
    }

    public static class Operator<T> {
        //函数也可以存在在数据结构中
        private BinaryOperator<T> operator ;
        private Operator(BinaryOperator<T> operator){
            this.operator = operator;
        }

        public static <T> Operator<T> from(
                BinaryOperator<T> operator){
            //函数可以作为参数进行传递
            return new Operator<>(operator);
        }

        public UnaryOperator<T> apply(T x) {
            //高阶函数  返回函数
            //x不可变，函数无副作用
            //闭包=函数 + 引用环境  返回函数被调用时，
            // x的值会被返回函数引用到，并进行惰性求值
            return (y) -> operator.apply(x, y);
        }
    }
}
