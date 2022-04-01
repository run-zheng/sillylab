package com.sillylab.lambda.master;

import java.awt.*;
import java.time.Year;
import java.util.List;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class StreamDemo4 extends MasterMain {
    private static final int MAX_DISTANCE = 2;

    public static void main(String[] args) {
        //1.8之前的积聚
        List<Book> bookList = new ArrayList<>();
        for (Book b : library()) {
            bookList.add(b);
        }
        //1.8的积聚
        library().stream().collect(Collectors.toList());
        //收集器模式的示例
        //根据主题对数进行分类的Map
        Map<Topic, List<Book>> bookByTopic = library().stream().collect(groupingBy(Book::getTopic));
        //从图书标题映射到最新版发布日期的有序Map
        library().stream()
                .collect(toMap(Book::getTitle,
                        Book::getPubDate,
                        BinaryOperator.maxBy(Comparator.naturalOrder()),
                        TreeMap::new));
        //将图书划分为小说与非小说的map
        Map<Boolean, List<Book>> fictionOrNon = library().stream()
                .collect(partitioningBy(b -> b.getTopic() == Topic.FICTION));
        //将每个主体关联到该主题下拥有最多作者的图书上
        Map<Topic, Optional<Book>> mostAuthorsByTopic = library().stream()
                .collect(groupingBy(Book::getTopic,
                        maxBy(Comparator.comparing(b -> b.getAuthors().size()))));
        //将每个主题关联到该主体的总的卷数上
        Map<Topic, Integer> volumnCountByTopic = library().stream()
                .collect(groupingBy(Book::getTopic,
                        summingInt(b -> b.getPageCounts().length)));
        //拥有最多图书的主题
        Optional<Topic> mostPopularTopic = library().stream()
                .collect(groupingBy(Book::getTopic, counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
        //将每个主体关联到该主题下所有图书标题拼接成的字符串上
        Map<Topic, String> concatenetedTitlesByTopic = library().stream()
                .collect(groupingBy(Book::getTopic,
                        mapping(Book::getTitle, joining(";"))));
        //收集流元素
        Map<String, Year> titleToPubDate = library().stream()
                .collect(toMap(Book::getTitle, Book::getPubDate));
        //收集流元素-键重复去重
        Map<String, Year> titleToPubDate2 = library().stream()
                .collect(toMap(Book::getTitle, Book::getPubDate, (x, y) -> x.isAfter(y) ? x : y));

        String join = library().stream().map(Book::getTitle).collect(Collectors.joining("::"));
        System.out.println(join);
        printLine();
        //创建一个字符串列表， 每个字符串包含一本书的所有作者名
        List<String> authorsForBooks = library().stream()
                .map(b -> b.getAuthors().stream()
                        .collect(Collectors.joining(", ", b.getTitle() + ": ", "")))
                .collect(Collectors.toList());
        System.out.println(authorsForBooks);
        printLine();
        //从图书标题映射到最新版本日期的Map
        Map<String, Year> titleToPubDate3 = library().stream()
                .collect(toMap(Book::getTitle,
                        Book::getPubDate,
                        (x, y) -> x.isAfter(y) ? x : y,
                        TreeMap::new));
        //将流收集到一个有序集合或者组赛队列  toList/toSet的重载方法
        TreeSet<String> sortedTitlesByTreeSet = library().stream()
                .map(Book::getTitle)
                .collect(Collectors.toCollection(TreeSet::new));
        LinkedBlockingQueue<Book> queueInPubDateOrder = library().stream()
                .sorted(Comparator.comparing(Book::getPubDate))
                .collect(Collectors.toCollection(LinkedBlockingQueue::new));
        //根据主题对图书进行分类
        Map<Topic, List<Book>> booksByTopic = library().stream().collect(groupingBy(Book::getTopic));
        //小说列表
        Map<Boolean, List<Book>> fictionOrNonFiction = library().stream()
                .collect(partitioningBy(b -> b.getTopic() == Topic.FICTION
                        || b.getTopic() == Topic.SCIENCE_FICTION));
        //groupingBy收集器相当于使用如下重载
        Map<Topic, List<Book>> booksByTopic2 = library().stream()
                .collect(groupingBy(Book::getTopic, toList()));
        //组合counting
        Map<Topic, Long> distributionByTopic = library().stream()
                .collect(groupingBy(Book::getTopic, counting()));
        //groupingBy的下游，必须是终止操作
        //对应终止操作max与min是Collectors工厂方法maxBy与minBy
        Map<Topic, Optional<Book>> mostAuthorsByTopic2 = library().stream()
                .collect(groupingBy(Book::getTopic,
                        maxBy(Comparator.comparing(b -> b.getAuthors().size()))));
        //对应于原生终止流操作sum与average的是summingInt, summingLong, summingDouble及其平均数版本返回的收集器
        Map<Topic, Integer> volumnCountByTopic2 = library().stream()
                .collect(groupingBy(Book::getTopic,
                        summingInt(b -> b.getPageCounts().length)));
        //对应于终止操作summaryStatistics的是summarizingInt/summarizingLong/summarizingDouble返回的收集器
        library().stream()
                .collect(groupingBy(Book::getTopic,
                        summarizingInt(b -> b.getPageCounts().length)));
        //对应于终止操作reduce的3个重载方法的是reducing的3个重载方法返回的收集器。

        //Java8增加用于处理Map.Entry对象组件的比较生成器方法comparingByKey与comparingByValue
        Optional<Topic> mostPopularTopic2 = library().stream()
                .collect(groupingBy(Book::getTopic, counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

        //最流行的主题
        Optional<Set<Topic>> mostPopularTopics = library().stream()
                .collect(groupingBy(Book::getTopic, counting()))
                .entrySet().stream()
                .collect(groupingBy(Map.Entry::getValue,
                        mapping(Map.Entry::getKey, toSet())))
                .entrySet().stream()
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue);

        //电力传输线路规划，塔间距至多是一个常量（MAX_DISTANCE)
        List<Point> pointList = new ArrayList<Point>() {{
            add(new Point(3, 0));
            add(new Point(6, 0));
            add(new Point(8, 0));
            add(new Point(10, 0));
            add(new Point(14, 0));
        }};
        //收集器的提供器
        Supplier<Deque<Deque<Point>>> supplier = () -> {
            Deque<Deque<Point>> ddp = new ArrayDeque<>();
            ddp.add(new ArrayDeque<>());
            return ddp;
        };
        //积聚器
        BiConsumer<Deque<Deque<Point>>, Point> accumulator = (ddp, p) -> {
            Deque<Point> last = ddp.getLast();
            if (!last.isEmpty() && last.getLast().distance(p) > MAX_DISTANCE) {
                Deque<Point> dp = new ArrayDeque<>();
                dp.add(p);
                ddp.add(dp);
            } else {
                last.add(p);
            }
        };

        //合并器
        BinaryOperator<Deque<Deque<Point>>> combiner = (left, right) -> {
            Deque<Point> leftLast = left.getLast();
            if (leftLast.isEmpty()) return right;
            Deque<Point> rightFirst = right.getFirst();
            if (rightFirst.isEmpty()) return left;
            Point p = rightFirst.getFirst();
            if (leftLast.getLast().distance(p) <= MAX_DISTANCE) {
                leftLast.addAll(rightFirst);
                right.removeFirst();
            }
            left.addAll(right);
            return left;
        };
        Deque<Deque<Point>> displacementRecords = pointList.stream()
                .collect(Collector.of(supplier, accumulator, combiner));
        //提供器
        Supplier<Deque<DispRecord>> supplier2 = ArrayDeque::new;
        //积聚器
        BiConsumer<Deque<DispRecord>, Book> accumulator2 = (dqleft, b) -> {
            int disp = dqleft.isEmpty() ? 0 : dqleft.getLast().totalDisp();
            dqleft.add(new DispRecord(b.getTitle(), disp, Arrays.stream(b.getPageCounts()).sum()));
        };
        //合并器
        BinaryOperator<Deque<DispRecord>> combiner2 = (left, right) -> {
            if(left.isEmpty()) return right;
            int newDisp = left.getLast().totalDisp();
            List<DispRecord> dispRecords = right.stream()
                    .map(r -> new DispRecord(r.title, r.disp+newDisp, r.length))
                    .collect(toList());
            left.addAll(dispRecords);
            return left ;
        };
        //完成器
        Function<Deque<DispRecord>, Map<String, Integer>> finisher2 =
                ddr -> ddr.parallelStream().collect(toConcurrentMap(dr -> dr.title, dr->dr.disp));
        Map<String, Integer> displacementMap = library().stream()
                .collect(Collector.of(supplier2, accumulator2, combiner2, finisher2));
    }

    public static class DispRecord {
        final String title ;
        final int disp, length;
        DispRecord(String t, int d, int l){
            this.title = t; this.disp = d; this.length = l ;
        }
        int totalDisp () {
            return disp + length;
        }
    }

    public Deque<Deque<Point>> groupByProximity(List<Point> sortedPointList) {
        Deque<Deque<Point>> points = new ArrayDeque<>();
        points.add(new ArrayDeque<>());
        for (Point p : sortedPointList) {
            Deque<Point> lastSegment = points.getLast();
            if (!lastSegment.isEmpty() &&
                    lastSegment.getLast().distance(p) > MAX_DISTANCE) {
                Deque<Point> newSegment = new ArrayDeque<>();
                newSegment.add(p);
                points.add(newSegment);
            } else {
                lastSegment.add(p);
            }
        }
        return points;
    }
}
