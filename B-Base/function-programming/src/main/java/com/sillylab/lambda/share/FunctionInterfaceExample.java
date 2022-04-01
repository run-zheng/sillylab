package com.sillylab.lambda.share;

@FunctionalInterface
public interface FunctionInterfaceExample<T> {
    void accept(T value);

    /**
     * java.lang.Object中的方法不是抽象方法
     * @param value
     * @return
     */
    @Override
    boolean equals(Object value);

    /**
     * 默认方法不是抽象方法
     */
    default void defaultMethod(){
        System.out.println("Hello world!");
    }

    /**
     * 静态方法不是抽象方法
     */
    static void staticMethod(){
    }
}
