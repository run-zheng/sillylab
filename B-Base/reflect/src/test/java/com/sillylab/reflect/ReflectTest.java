package com.sillylab.reflect;

import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @author run-zheng
 * @date 2022/4/8 17:18
 */
public class ReflectTest {
    public static class ReturnTypeTest<T> {
        public T returnT() {
            return null;
        }
    }

    public static class ReturnTypeSubClass extends ReturnTypeTest<String>{
        public void returnNone(){

        }
    }
    @Test
    public void test() throws NoSuchMethodException {
        Method method = ReturnTypeTest.class.getMethod("returnT");
        System.out.println(method.getReturnType());
        System.out.println(method.getGenericReturnType());
        System.out.println(method.getAnnotatedReturnType());
        method = ReturnTypeSubClass.class.getMethod("returnT");
        System.out.println(method.getReturnType());
        System.out.println(method.getGenericReturnType());
        System.out.println(method.getAnnotatedReturnType());
        method = ReturnTypeSubClass.class.getMethod("returnNone");
        System.out.println(method.getReturnType());
        System.out.println(String.valueOf(method.getReturnType()).equals("void"));
        System.out.println(method.getGenericReturnType());
        System.out.println(method.getAnnotatedReturnType());

    }

}
