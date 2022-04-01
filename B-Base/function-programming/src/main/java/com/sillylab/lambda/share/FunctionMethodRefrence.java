package com.sillylab.lambda.share;

import com.sillylab.lambda.Common;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FunctionMethodRefrence extends Common {

    public static void main(String[] args) {
        //构造方法引用
        Supplier<ClassRef> aSupplierRef = ClassRef::new;
        aSupplierRef = () -> new ClassRef();
        Function<Integer, ClassRef> aNewRef = ClassRef::new;
        aNewRef = i -> new ClassRef(i);
        BiFunction<Integer, String, ClassRef> aNewRef2 = ClassRef::new;
        aNewRef2 = (i, s) -> new ClassRef(i, s);

        FunctionMethodRefrence.fromSupplier(ClassRef::new);
        FunctionMethodRefrence.<Integer, ClassRef>fromFunction(ClassRef::new);
        FunctionMethodRefrence.<Integer, String, ClassRef>fromBiFunction(ClassRef::new);
        printLine();
        //静态方法引用
        Consumer<String> consumer = ClassRef::staticMethod;
        consumer = s -> ClassRef.staticMethod(s);
        FunctionMethodRefrence.fromConsumer(ClassRef::staticMethod);
        printLine();
        //对象方法引用
        ClassRef ref = new ClassRef();
        Supplier<String> instRef = ref::instMethod;
        instRef = () -> ref.instMethod();
        FunctionMethodRefrence.fromSupplier(ref::instMethod);
        Supplier<String> instSuperRef = ref.fromSuper();
        instSuperRef = () -> ref.superMethod();
        FunctionMethodRefrence.fromSupplier(ref.fromSuper());
        printLine();
        //实例方法引用
        ClassRef.fromOther(ClassRef::instMethod);
        ClassRef.fromOther(r -> r.instMethod());
    }
    public static <T> void fromSupplier(Supplier<T> supplier){
        System.out.println(supplier.get());
    }
    public static <T, R> void fromFunction(Function<T, R> function){
    }
    public static <T,U, R> void fromBiFunction(BiFunction<T,U, R> Bifunction){
    }
    public static <T> void fromConsumer(Consumer<T> consumer){
    }

    public static class SuperClass {
        public String superMethod(){
            return "From Super";
        }
    }

    public static class ClassRef extends SuperClass {
        public ClassRef(){

        }
        public ClassRef(Integer integer){
        }
        public ClassRef(Integer integer, String str){
        }

        public static void staticMethod(String str){
        }
        public String instMethod(){
            return "Hello World!";
        }
        public Supplier<String> fromSuper(){
            return super::superMethod;
        }

        public static String fromOther(ClassRefFilter ref){
            ClassRef classRef = new ClassRef();
            return ref.accept(classRef);
        }
    }
    @FunctionalInterface
    public static interface ClassRefFilter {
        String accept(ClassRef ref);
    }
}
