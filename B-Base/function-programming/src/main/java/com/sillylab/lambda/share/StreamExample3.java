package com.sillylab.lambda.share;

import com.sillylab.lambda.master.MasterMain;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamExample3 extends MasterMain {
    public static void main(String[] args) {
        //流基础，iterate生成无限流
        IntStream.iterate(1, i -> i * 2)
                .limit(10)
                .forEachOrdered(System.out::println);
        //原生类型流 频繁装箱/拆箱
        OptionalInt max= //Arrays.asList(1, 2, 3, 4, 5).stream()
                Arrays.stream(new int[]{1, 2, 3, 4, 5})
                .map(i -> i + 1)
                .max();
        //每个原生类型流都有一个boxed方法
        Stream<Integer> is = IntStream.rangeClosed(1, 10).boxed();
        //拆箱的操作可以通过映射转换操作转换成原生流
        IntStream intStream = Stream.of(1, 2).mapToInt(Integer::intValue);

        //
        List<Book> library = library();
        //只包含计算过的图书
        Stream<Book> computingBooks = library.stream().filter(b -> b.getTopic() == Topic.COMPUTING);
        //图书标题流
        Stream<String> bookTitles = library.stream().map(Book::getTitle);
        //book流，根据标题排序
        Stream<Book> booksSortedByTitle = library.stream().sorted(Comparator.comparing(Book::getTitle));
        //使用排序流创建作者流，根据图书标题排序并去重
        Stream<String> authorsInBookTitleOrder = library.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .flatMap(book -> book.getAuthors().stream())
                .distinct();
        //以标题的字母顺序生成前100个图书流
        Stream<Book> readingList = library.stream().sorted(Comparator.comparing(Book::getTitle)).limit(100);
        //除去前100个图书流
        Stream<Book> remainderList = library.stream().sorted(Comparator.comparing(Book::getTitle)).skip(100);
        //最早出版的图书
        Optional<Book> oldest = library.stream()
                .min(Comparator.comparing(Book::getPubDate));
        //图书标题集合
        Set<String> titles = library.stream().map(Book::getTitle).collect(Collectors.toSet());
        //映射出版年份
        Stream<Year> booksPubDate = library.stream().map(Book::getPubDate);
        //映射原生流，计算作者数量
        int totalAuthorships = library.stream().mapToInt(b -> b.getAuthors().size()).sum();
        //一对多的映射
        Stream<String> authorStream = library.stream().flatMap(b -> b.getAuthors().stream());
        //一对多映射原生流
        int totalPageCount = library.stream().flatMapToInt(b -> IntStream.of(b.getPageCounts())).sum();
        //调试
        List<Book> multipleAuthoredHistories = library.stream()
                .filter(b -> b.getTopic() == Topic.HISTORY)
                .peek(b -> System.out.println(b.getTitle()))
                .filter(b -> b.getAuthors().size() > 1)
                .collect(Collectors.toList());
        //排序和去重
        Stream<String> sortedTitles = library.stream().map(Book::getTitle).sorted();
        Stream<Book> bookSortedByTitle = library.stream().sorted(Comparator.comparing(Book::getTitle));
        Stream<Book> booksSortedByAuthorCount = library.stream()
                .sorted(Comparator.comparing(Book::getAuthors,
                    Comparator.comparing(List::size)));
        //搜索操作
        boolean withinShelfHeight = library.stream()
                .filter(b -> b.getTopic() == Topic.HISTORY)
                .allMatch(b -> b.getHeight() < 19);
        Optional<Book> anyBook = library.stream()
                .filter(b -> b.getAuthors().contains("Herman Melville"))
                .findAny();
    }

}
