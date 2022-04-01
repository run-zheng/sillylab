package com.sillylab.lambda.share;

import com.sillylab.lambda.Common;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

public class StreamExample5 extends Common {


    public static void main(String[] args) {
        //用流收集数据
        Map<Currency, List<Transaction>> transactionsByCurrencies =
                transactions.stream().collect(groupingBy(Transaction::getCurrency));
        //==============================归约汇总===============================
        //查找最大值最小值
        Comparator<Dish> dishCaloriesComprater =
                comparingInt(Dish::getCalories);
        Optional<Dish> mostCalorieDish = menu.stream().collect(maxBy(dishCaloriesComprater));
        //汇总
        int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
        //计算平均数
        double avgCalories =
                menu.stream().collect(averagingInt(Dish::getCalories));
        //计算统计值:总和、平均值、最大值和最小值
        IntSummaryStatistics menuStatistics =
                menu.stream().collect(summarizingInt(Dish::getCalories));
        //字符串链接
        String shortMenu = menu.stream().map(Dish::getName).collect(joining());
        String shortMenu2 = menu.stream().map(Dish::getName).collect(joining(","));
        //更通用的归约函数
        int totalCalories2 = menu.stream().collect(reducing(
                0, Dish::getCalories, (i, j) -> i + j));

        int totalCalories3 = menu.stream().collect(reducing(0,
                Dish::getCalories,
                Integer::sum));
        //==============================归约汇总  end===============================

        //==============================分组===============================
        //   按类型分组
        Map<Dish.Type, List<Dish>> dishesByType =
                menu.stream().collect(groupingBy(Dish::getType));
        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(
                groupingBy(dish -> {
                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                    else if (dish.getCalories() <= 700) return
                            CaloricLevel.NORMAL;
                    else return CaloricLevel.FAT;
                }));
        //多级分组
        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel =
                menu.stream().collect(
                        groupingBy(Dish::getType,
                                groupingBy(dish -> {
                                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                                    else return CaloricLevel.FAT;
                                })));
        //按子组收集数据
        Map<Dish.Type, Long> typesCount = menu.stream().collect(
                groupingBy(Dish::getType, counting()));
        //
        Map<Dish.Type, Optional<Dish>> mostCaloricByType =
                menu.stream()
                        .collect(groupingBy(Dish::getType,
                                maxBy(comparingInt(Dish::getCalories))));

        //收集器的结果转换为另一种类型
        Map<Dish.Type, Dish> mostCaloricByType2 =
                menu.stream()
                        .collect(groupingBy(Dish::getType,
                                collectingAndThen(maxBy(comparingInt(Dish::getCalories)), Optional::get)));
        //
        Map<Dish.Type, Integer> totalCaloriesByType =
                menu.stream().collect(groupingBy(Dish::getType,
                        summingInt(Dish::getCalories)));
        //gr upingBy联合使用的其他收集器
        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType =
                menu.stream().collect(
                        groupingBy(Dish::getType, mapping(
                                dish -> {
                                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                                    else return CaloricLevel.FAT;
                                }, toSet())));
        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType2 =
                menu.stream().collect(
                        groupingBy(Dish::getType, mapping(
                                dish -> {
                                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                                    else return CaloricLevel.FAT;
                                }, toCollection(HashSet::new))));
        //==============================分组  end===============================

        //==============================分区==============================
        Map<Boolean, List<Dish>> partitionedMenu =
                menu.stream().collect(partitioningBy(Dish::isVegetarian));

        Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType =
                menu.stream().collect(
                        partitioningBy(Dish::isVegetarian,
                                groupingBy(Dish::getType)));
    }

    public enum CaloricLevel {DIET, NORMAL, FAT}

    public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {
        @Override
        public Supplier<List<T>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<T>, T> accumulator() {
            return List::add;
        }

        @Override
        public BinaryOperator<List<T>> combiner() {
            return (list1, list2) -> {
                list1.addAll(list2);
                return list1;
            };
        }

        @Override
        public Function<List<T>, List<T>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.unmodifiableSet(EnumSet.of(
                    Characteristics.IDENTITY_FINISH,
                    Characteristics.CONCURRENT));
        }

    }
}
