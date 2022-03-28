package com.sillylab.lambda.master;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class LambdaDemo1 {
    public static void main(String[] args) {
        List<Point> pointList = Arrays.asList(new Point(1, 2), new Point(2, 3));
        //## 外部迭代到内部迭代
        //一般写法
        for (Point p :pointList){
            p.translate(1, 1);
            System.out.println(p);
        }
        printLine();

        //内部迭代
        //pointList.forEach(....);

        //## 命令模式
        //命令式写法
        PointArrayList pointArrayList = new PointArrayList();
        pointArrayList.addAll(Arrays.asList(new Point(1, 2), new Point(2, 3)));
        pointArrayList.forEach(new TranslateByOne());
        printLine();
        //Java8的函数式接口
        //Iterable接口的forEach方法，接受java.util.function.Consumer函数式接口
        List<Point> java8List = Arrays.asList(new Point(1, 2), new Point(2, 3));
        java8List.forEach(new TranslateByOne2());
        printLine();

        //## lambda表达式
        //匿名内部类写法
        List<Point> anonymousList = Arrays.asList(new Point(1, 2), new Point(2, 3));
        anonymousList.forEach(new Consumer<Point>() {
            @Override
            public void accept(Point point) {
                point.translate(1, 1);
                System.out.println(point);
            }
        });
        printLine();
        //lambda表达式写法
        List<Point> lambdaList = Arrays.asList(new Point(1, 2), new Point(2, 3));
        lambdaList.forEach(p -> {
            p.translate(1, 1);
            System.out.println(p);
        });
        printLine();

        //## 从集合到流
        // 一般写法
        List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5);
        List<Point> pointList2 = new ArrayList<>();
        for(Integer i: intList){
            pointList2.add(new Point(i % 3, i / 3));
        }

        double maxDistance = Double.MIN_VALUE;
        for (Point p : pointList2){
            maxDistance = Math.max(p.distance(0, 0), maxDistance);
        }
        System.out.println(maxDistance);
        printLine();
        //Stream 写法
        Stream<Point> pointStream = intList.stream().map(i -> new Point(i % 3, i / 3));
        DoubleStream distanceStream = pointStream.mapToDouble(p -> p.distance(0, 0));
        OptionalDouble distance = distanceStream.max();
        System.out.println(distance.getAsDouble());
        printLine();
        //合起来写
        distance = intList.stream()
                .map(i -> new Point(i % 3, i / 3))
                .mapToDouble(p -> p.distance(0, 0))
                .max();
        System.out.println(distance.getAsDouble());
        printLine();

        //## 从串行到并行流
        //任务分解伪代码
        // if the task list contains more than N/4 elements {
        //    leftTask = task.getLeftHalf()
        //    rightTask = task.getRightHalf()
        //    doInParallel {
        //      leftResult = leftTask.solve()
        //      rightResult = rightTask.solve()
        //    }
        //    result = combine(leftResult, rightResult)
        // } else {
        //   result = task.solveSequentially()
        // }
        //并行流写法
        distance = intList.parallelStream()
                .map(i -> new Point(i % 3, i / 3))
                .mapToDouble(p -> p.distance(0, 0))
                .max();
        System.out.println(distance.getAsDouble());
        printLine();

        //## 组合写法
        //按照x坐标对Point列表进行排序
        Comparator<Point> byX = new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                return Double.compare(o1.getX(), o2.getX());
            }
        };
        //lambda写法
        Comparator<Point> byX2 = (o1, o2) -> Double.compare(o1.getX(), o2.getX());
        //去掉Comparator的写法
        Function<Point, Double> keyExtractor = p -> p.getX();
        Comparator<Double> keyComparator = (d1, d2) -> Double.compare(d1, d2);
        Comparator<Point> comparatorByX = (p1, p2) ->
                keyComparator.compare(keyExtractor.apply(p1), keyExtractor.apply(p2));
        //泛化的comparing写法 (高价函数，参数是函数、返回值也是函数)
        int compareByX = comparing((Point p) -> p.getX()).compare(new Point(1, 2), new Point(2, 3));
        System.out.println(compareByX);
        int compareByDistance = comparing((Point p) -> p.distance(0, 0)).compare(new Point(1, 2), new Point(2, 1));
        System.out.println(compareByDistance);
        intList.stream()
                .map(i -> new Point(i % 3, i / 3))
                .sorted(comparing(p -> p.distance(0, 0)))
                .forEach(p -> System.out.printf("(%f, %f)\n", p.getX(), p.getY()));
        printLine();
    }

    /**
     * 泛化的comparing写法
     */
    public static <T, U extends Comparable<U>> Comparator<T> comparing(Function<T, U> keyExtractor){
        return (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
    }


    public static void printLine(){
        System.out.println("-------------------------------------------");
    }
    /**
     * 命令式写法：action
     */
    public interface PointAction {
        void doForPoint(Point p);
    }
    /**
     * 命令式写法：action实现
     */
    public static class TranslateByOne implements PointAction{
        public void doForPoint(Point p ){
            p.translate(1, 1);
            System.out.println(p);
        }
    }

    /**
     * 命令式写法：Point列表扩展
     */
    public static class PointArrayList extends ArrayList<Point> {
        public void forEach(PointAction t){
            for(Point p : this){
                t.doForPoint(p);
            }
        }
    }

    /**
     * Java8函数式接口： 实现Consumer
     */
    public static class TranslateByOne2 implements Consumer<Point>{
        public void accept(Point p ){
            p.translate(1, 1);
            System.out.println(p);
        }
    }
}
